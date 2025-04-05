
import javax.swing.*;
        import java.awt.*;
        import java.awt.event.*;
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

        queueLabel.setEditable(false);              // 不允许编辑
        queueLabel.setLineWrap(true);               // 自动换行
        queueLabel.setWrapStyleWord(true);          // 以单词边界换行（更美观）

        JScrollPane scrollPane = new JScrollPane(queueLabel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 总是显示垂直滚动条
        scrollPane.setPreferredSize(new Dimension(350, 200)); // 可以自定义尺寸


        statusPanel.add(scrollPane);

        // --- 显示：已完成和已送达 ---
        JPanel midPanel = new JPanel(new GridLayout(1, 2));

        readyToServeArea.setEditable(false);
        readyToServeArea.setLineWrap(true);
        JScrollPane readyScrollPane = new JScrollPane(readyToServeArea);

        deliveredOrdersArea.setEditable(false);
        deliveredOrdersArea.setLineWrap(true);
        JScrollPane deliveredScrollPane = new JScrollPane(deliveredOrdersArea);
        
        midPanel.add(readyScrollPane);
        midPanel.add(deliveredScrollPane);
        
        statusPanel.add(midPanel);

        JPanel threadPanel = new JPanel(new GridLayout(1,4));

        String[] staffNames = { "Cook 1", "Cook 2","Server 1", "Server 2",};
        for (int i = 0; i < 4; i++) {
            serverStatus[i] = new JTextArea(staffNames[i] + " status: free");
            threadPanel.add(serverStatus[i]);
        }
        statusPanel.add(threadPanel);
        
        JButton addCustomerBtn = new JButton("OPEN Cafe");
        addCustomerBtn.addActionListener(e -> {



            queueLabel.setText("There are currently "+ GUIOrderManager.getSize() +
                    " people waiting in the queue：" + "\n" + result);
            addCustomerBtn.setVisible(false);
            String[] staffThreadNames = {"Cook 1", "Cook 2","Server 1", "Server 2", };
            Object lock = new Object();
            for (int i = 0; i < 2; i++) {
                Cook s = new Cook(staffThreadNames[i], serverStatus[i],lock);
                new Thread(s).start();
            }

            for (int i = 2; i < 4; i++) {
                Server s = new Server(staffThreadNames[i], serverStatus[i],orderManager,lock);
                new Thread(s).start();
            }


        });

//        JButton switchToReportBtn = new JButton("see report");
//        switchToReportBtn.addActionListener(e -> cardLayout.show(cardPanel, "report"));

        JPanel controlPanel = new JPanel();
        controlPanel.add(addCustomerBtn);
//        controlPanel.add(switchToReportBtn);

        JPanel card1 = new JPanel(new BorderLayout());
        card1.add(statusPanel, BorderLayout.CENTER);
        card1.add(controlPanel, BorderLayout.SOUTH);

//        // === 卡片2：报告 ===
//        reportArea.setEditable(false);
//        JScrollPane reportScroll = new JScrollPane(reportArea);
//
//        JButton backToStatusBtn = new JButton("返回状态");
//        backToStatusBtn.addActionListener(e -> cardLayout.show(cardPanel, "status"));
//
//        JPanel card2 = new JPanel(new BorderLayout());
//        card2.add(reportScroll, BorderLayout.CENTER);
//        card2.add(backToStatusBtn, BorderLayout.SOUTH);
//
//        // === 卡片面板组装 ===
        cardPanel.add(card1, "status");
//        cardPanel.add(card2, "report");
//
        add(cardPanel, BorderLayout.CENTER);
//
        // === 启动服务线程 ===

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                // 更新 readyToServeArea
                StringBuilder readyText = new StringBuilder("Ready Orders:\n");
                for (Order order : ServerOrderManager.getOrderList()) {
                readyText.append("Order ").append(order.getID()).append(" - ")
                    .append("Customer: ").append(orderManager.getCustomerByOrder(order.getID()).getName()).append("\n");
                }
                readyToServeArea.setText(readyText.toString());
                
                // 更新 deliveredOrdersArea
                StringBuilder deliveredText = new StringBuilder("Delivered Orders:\n");
                for (Order order : DeliveredOrderManager.getDeliveredOrders()) {
                    deliveredText.append("Order ").append(order.getID()).append(" - ")
                        .append("Customer: ").append(orderManager.getCustomerByOrder(order.getID()).getName()).append("\n");
                }
            deliveredOrdersArea.setText(deliveredText.toString());
            });
        }, 0, 1, TimeUnit.SECONDS); // 每1秒刷新

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
        // 2. 放入 Queue（可选用 LinkedList 或 PriorityQueue）
        return result;

    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(CafeThreadGUI::new);
    }
}
