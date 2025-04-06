
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class CafeThreadGUI extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel = new JPanel(cardLayout);
    private JTextArea readyToServeArea = new JTextArea("Ready Orders:\n");
    private JTextArea deliveredOrdersArea = new JTextArea("Delivered Orders:\n");


    private JTextArea queueLabel = new JTextArea("current waiting orders：0");
    private JTextArea[] serverStatus = new JTextArea[4];
    private JTextArea reportArea = new JTextArea();
    private OrderManager orderManager;
    public CafeThreadGUI() {

        orderManager = new OrderManager();
        String result = SortOrderListToMap(orderManager.getOrderList());
        setTitle("CafeThreadGUI");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === 卡片1：实时状态 ===
        JPanel statusPanel = new JPanel(new GridLayout(2, 1));
        JPanel subStatusPanel = new JPanel(new GridLayout(1, 2));

        queueLabel.setEditable(false);              // editable
        queueLabel.setLineWrap(true);               // cr
        queueLabel.setWrapStyleWord(true);          //

        JPanel scrollPane = new JPanel(new BorderLayout());
        scrollPane.add(queueLabel);



        subStatusPanel.add(scrollPane);

        // --- ui：finished & delivered ---
        JPanel midPanel = new JPanel(new GridLayout(1, 2));

        readyToServeArea.setEditable(false);
        readyToServeArea.setLineWrap(true);
        JScrollPane readyScrollPane = new JScrollPane(readyToServeArea);

        deliveredOrdersArea.setEditable(false);
        deliveredOrdersArea.setLineWrap(true);
        JScrollPane deliveredScrollPane = new JScrollPane(deliveredOrdersArea);
        
        midPanel.add(readyScrollPane);
        midPanel.add(deliveredScrollPane);

        subStatusPanel.add(midPanel);
        statusPanel.add(subStatusPanel);
        JPanel threadPanel = new JPanel(new GridLayout(1,4));

        String[] staffNames = { "Cook 1", "Cook 2","Server 1", "Server 2",};
        for (int i = 0; i < 4; i++) {
            serverStatus[i] = new JTextArea(staffNames[i] + " status: free");
            threadPanel.add(serverStatus[i]);
        }
        statusPanel.add(threadPanel);
        Thread[] serverThreads = new Thread[2];
        JButton addCustomerBtn = new JButton("OPEN Cafe");
        addCustomerBtn.addActionListener(e -> {

            Logger.getInstance().log("Cafe open.\n");

            queueLabel.setText("There are currently "+ GUIOrderManager.getSize() +
                    " people waiting in the queue：" + "\n" + result);

            Logger.getInstance().log("Customer is added into queue.\n");

            addCustomerBtn.setVisible(false);
            String[] staffThreadNames = {"Cook 1", "Cook 2","Server 1", "Server 2", };
            Object lock = new Object();


            for (int i = 0; i < 2; i++) {
                Cook s = new Cook(staffThreadNames[i], serverStatus[i],lock,orderManager);
                new Thread(s).start();
            }

            for (int i = 2; i < 4; i++) {
                Server s = new Server(staffThreadNames[i], serverStatus[i],orderManager,lock);
                serverThreads[i-2] = new Thread(s);
                serverThreads[i-2].start();
            }



            new Thread(() -> {
                try {
                    for (int i = 0; i < 2; i++) {
                        if (serverThreads[i] != null) {
                            serverThreads[i].join();  // same time end
                            System.out.println("Thread " + i + " finished");
                        } else {
                            System.out.println("Thread " + i + " is null");
                        }
                    }

                    // Generate report and log
                    ReportGenerator.countInformation(orderManager);
                    ReportGenerator.printReport("report.txt");
                    Logger.getInstance().log("Complete generating report and log.\n");
                    Logger.getInstance().saveToFile();

                    UIManager.put("OptionPane.okButtonText", "OK");
                    JOptionPane.showMessageDialog(this, 
                        "All threads finished, updating the UI and closing application in 10 seconds...", 
                        "Info", 
                        JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("All threads finished, updating the UI and closing application in 10 seconds...");

                    // === 10 s exit ===
                    ScheduledExecutorService shutdownExecutor = Executors.newSingleThreadScheduledExecutor();
                    shutdownExecutor.schedule(() -> {
                        SwingUtilities.invokeLater(() -> {
                            dispose(); // windows exit
                            System.exit(0); // exit
                        });
                    }, 10, TimeUnit.SECONDS);
                } catch (InterruptedException | IOException ex) {
                    ex.printStackTrace();
                }
            }).start();  // EDT




        });



        JPanel controlPanel = new JPanel();
        controlPanel.add(addCustomerBtn);
//        controlPanel.add(switchToReportBtn);

        JPanel card1 = new JPanel(new BorderLayout());
        card1.add(statusPanel, BorderLayout.CENTER);
        card1.add(controlPanel, BorderLayout.SOUTH);


        cardPanel.add(card1, "status");
//        cardPanel.add(card2, "report");
//
        add(cardPanel, BorderLayout.CENTER);
//
        // === start refreshing waiting panel thread ===

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {

                // ui: Waiting to Cook Orders
                StringBuilder waitingText = new StringBuilder("Waiting to Cook Orders:\n");
               List<Order> waitingOrders = new ArrayList<>(CookOrderManager.getOrderList());

                synchronized (waitingOrders) {
                    for (Order order : waitingOrders) {
                        if (!order.isPoisonPill()) {
                            waitingText.append("Order ").append(order.getID()).append(" - ")
                                    .append("Customer: ").append(orderManager.getCustomerByOrder(order.getID()).getName())
                                    .append("\n");
                        }
                    }
                }

                queueLabel.setText(waitingText.toString());

                // ui : update: readyToServeArea
                StringBuilder readyText = new StringBuilder("Waiting for Serverd Orders:\n");
                List<Order> orders = new ArrayList<>(ServerOrderManager.getOrderList());
                if (orders !=null){
                    for (Order order : orders) {
                        if(!order.isPoisonPill()) readyText.append("Order ").append(order.getID()).append(" - ")
                                .append("Customer: ").append(orderManager.getCustomerByOrder(order.getID()).getName()).append("\n");
                    }
                    readyToServeArea.setText(readyText.toString());
                }

                
                // UI:  deliveredOrdersArea
                StringBuilder deliveredText = new StringBuilder("Delivered Orders:\n");
                List<Order> orders2 = new ArrayList<>(DeliveredOrderManager.getDeliveredOrders());
                if(orders2!=null){

                    for (Order order :orders2) {
                        if(!order.isPoisonPill()) deliveredText.append("Order ").append(order.getID()).append(" - ")
                                .append("Customer: ").append(orderManager.getCustomerByOrder(order.getID()).getName()).append("\n");
                    }
                    deliveredOrdersArea.setText(deliveredText.toString());
                }

            });
        }, 0, 100, TimeUnit.MILLISECONDS); // 0.1 s refresh rate

                // 时间控制面板
        JPanel timeControlPanel = new JPanel();
        JLabel multiplierLabel = new JLabel();
        TimeManager.bindLabel(multiplierLabel);  // rate  JLabel

        JButton speed05 = new JButton("x0.5");
        JButton speed025 = new JButton("x0.25");
        JButton speed1 = new JButton("x1");
        JButton speed15 = new JButton("x1.5");
        JButton speed2 = new JButton("x2");

        speed05.addActionListener(e -> TimeManager.setTimeMultiplier(0.5));
        speed025.addActionListener(e -> TimeManager.setTimeMultiplier(0.25));
        speed1.addActionListener(e -> TimeManager.setTimeMultiplier(1.0));
        speed15.addActionListener(e -> TimeManager.setTimeMultiplier(1.5));
        speed2.addActionListener(e -> TimeManager.setTimeMultiplier(2.0));

        timeControlPanel.add(new JLabel("Time Scale Control: (Faster <--)"));
        timeControlPanel.add(speed025);
        timeControlPanel.add(speed05);
        timeControlPanel.add(speed1);
        timeControlPanel.add(speed15);
        timeControlPanel.add(speed2);
        timeControlPanel.add(multiplierLabel);

        add(timeControlPanel, BorderLayout.SOUTH);

        setVisible(true);




    }

    private String SortOrderListToMap(List<List<Order>> orders) {
        List<Order> sorted = orders.stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparing(Order::getParsedTime))
                .collect(Collectors.toList());


            for (Order order: sorted){
                GUIOrderManager.addOrder(order);
                CookOrderManager.addOrder(order);

            }

        StringBuilder sb = new StringBuilder();

        for (GUIOrderNode entry : GUIOrderManager.getOrderMap().values()) {
            Order order = entry.getOrder();
            String customerName = orderManager.getCustomerByOrder(order.getID()).getName();
            int totalItemCount = order.getOrder().size();

            sb.append("orderID: ").append(order.getID())
                    .append("\tcustomer name: ").append(customerName)
                    .append("\t\t\titem : ").append(totalItemCount)
                    .append("\n");
        }

        String result = sb.toString();

        return result;

    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(CafeThreadGUI::new);
    }
}
