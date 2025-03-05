
import javax.swing.*;
        import java.awt.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ShopGUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextArea orderBill;
    private JTextField customerNameField;
    private Map<MenuItem, Integer> billList;

    private HashMap<String, MenuItem> shopList;
    private List<JButton> itemButtons = new ArrayList<>();
    private int currentCustomerId;
    private int currentOrderId;
    private String customerName;
    private JButton confirmNameButton;
    private JTextArea summaryText;
    private OrderManager orderManager;
    private JTextField customerHintField;


    public ShopGUI()   {
        frame = new JFrame("ASECoffee Shop ~ Welcome to our shop");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        billList = new HashMap<>();


        shopList = DataLoader.loadMenu();
        orderManager = new OrderManager();
        currentCustomerId = orderManager.incrementCustomerId();
        currentOrderId = orderManager.incrementOrderId();


        mainPanel.add(new BackgroundPanel("menu.png", createMenuPanel()), "Menu");
        mainPanel.add(createOrderPanel(), "Order");
        mainPanel.add(new BackgroundPanel("back.png",createBillPanel()), "Bill");
        mainPanel.add(new BackgroundPanel("back.png",createSummaryPanel()), "Summary");


        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // 让背景可见
        JTextArea menuText = new JTextArea("\n\n\t\tMenu:\n");
        for (MenuItem item : shopList.values()) {
            menuText.append("\t\t"+item.getName() + " - price:" + item.getCost() + "\n");
        }
        menuText.setEditable(false);
        menuText.setOpaque(false);
        panel.add(menuText, BorderLayout.CENTER);

        JButton nextButton = new JButton("GO TO ORDER");
        nextButton.addActionListener(e -> cardLayout.show(mainPanel, "Order"));
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrderPanel() {


        JPanel ALLPanel = new JPanel();
        ALLPanel.setOpaque(false); // 让背景可见
        ALLPanel.setLayout(new BoxLayout(ALLPanel, BoxLayout.Y_AXIS));


        JPanel flowPanel =  new JPanel(new GridLayout(shopList.size(), 1));
        JPanel backPanel =  new BackgroundPanel("menu.png",flowPanel);
        flowPanel.setOpaque(false);
//        flowPanel.setBackground(Color.pink);

        JPanel namePanel = new JPanel(new BorderLayout());
        namePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        customerHintField = new JTextField("please input your name");
        customerNameField = new JTextField();
        confirmNameButton = new JButton("√ CONFIRM");
        confirmNameButton.addActionListener(e -> toggleItemButtons());
        namePanel.add(customerHintField, BorderLayout.WEST);
        namePanel.add(customerNameField, BorderLayout.CENTER);
        namePanel.add(confirmNameButton, BorderLayout.EAST);
        ALLPanel.add(namePanel);

        for (MenuItem item : shopList.values()) {
            JPanel itemPanel = new JPanel(new FlowLayout());
//            display
            JTextField quantityField = new JTextField("0", 3);
            quantityField.setOpaque(false);
            quantityField.setEnabled(false);
//            hash map add item and quantity field
//            quantityFields.put(item.getIdentifier(), quantityField);
//            add button
            JButton button = new JButton(item.getName() + " ($" + item.getCost() + ") +");
            JButton buttonDelete = new JButton("-");
            buttonDelete.addActionListener(e -> deleteToOrder(item,quantityField));

            button.addActionListener(e -> addToOrder(item,quantityField));

            button.setEnabled(false);

            itemPanel.add(buttonDelete);
            itemButtons.add(button);
            itemPanel.add(quantityField);
            itemPanel.add(button);
            itemPanel.setOpaque(false);
//            - item + amount
            flowPanel.add(itemPanel);


        }

    JScrollPane scrollPane = new JScrollPane(backPanel);
    scrollPane.setOpaque(false);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

    ALLPanel.add(scrollPane);
    JButton checkoutButton = new JButton("CKECK YOUR ORDER");
        checkoutButton.addActionListener(e -> {
            customerName = customerNameField.getText().trim();
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "input your name before order!");
            } else {
                updateBill();
                cardLayout.show(mainPanel, "Bill");
            }
        });
        ALLPanel.add(checkoutButton);

        return ALLPanel;
    }

    private void addToOrder(MenuItem item, JTextField quantityField) {
        //        exist
        if (billList.containsKey(item)) {
            int quantity = billList.get(item);
            quantity++;
            billList.put(item, quantity);
            quantityField.setText(String.valueOf(quantity));

        } else {
            billList.put(item, 1);
            quantityField.setText(String.valueOf(1));

        }
    }

    private void deleteToOrder(MenuItem item, JTextField quantityField) {

        if(billList.containsKey(item)) {
            int quantity = billList.get(item);
            if (quantity > 1) {
                quantity--;
                billList.put(item, quantity);
                quantityField.setText(String.valueOf(quantity));

            } else if (quantity == 1) {
                billList.remove(item);
                quantityField.setText("0");
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "already no items！");
        }
    }




    private void toggleItemButtons() {
        String name = customerNameField.getText().trim();
        boolean enable = !name.isEmpty();
        for (JButton button : itemButtons) {
            button.setEnabled(enable);
        }
        if(enable) {
            customerNameField.setEditable(false);
        }
        else {
            customerNameField.setEditable(true);
        }

    }
    private JPanel createBillPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false); // 让背景可见
        orderBill = new JTextArea();
        orderBill.setEditable(false);
        orderBill.setOpaque(false);

        panel.add(orderBill, BorderLayout.CENTER);

        JButton backButton = new JButton("See Summary");
        backButton.addActionListener(e -> {
            displaySummary();
            cardLayout.show(mainPanel, "Summary");

        });
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }



    private void updateBill() {
        StringBuilder Bill = new StringBuilder("bill receipt:\n");
        for (MenuItem item : billList.keySet()) {
            Bill.append(item.getName()).append(" x ").append(billList.get(item)).append("\n");
        }
        DataLoader.writeACustomer(new Customer(currentCustomerId, customerName));
        Order order = new Order(currentOrderId, billList,currentCustomerId, LocalDateTime.now().toString(),
                false, 0.0F,false,0, 0);
        order.getPrize();
//        TODO: write to file
        orderManager.addOrder(order);
        DataLoader.writeOrder(order);
//

        Bill.append("------------------------\n");

        Bill.append("customer name: ").append(customerName).append("\n");
        Bill.append("total price: $").append(String.format("%.2f", order.getPrize())).append("\n");
        Bill.append("total discount: $").append(String.format("%.2f", order.getTotalDiscount())).append("\n");

        orderBill.setText(Bill.toString());
    }


    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        summaryText = new JTextArea("Summary:\n");
        summaryText.setEditable(false);
        summaryText.setOpaque(false);




        panel.add(summaryText, BorderLayout.CENTER);


//        JButton backButton = new JButton("返回菜单");
//        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
//        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }
    private void displaySummary() {
//        所有菜品订购次数
//        订单总收入
        float totalIncome = 0;
        float totalDiscount = 0;
        HashMap<MenuItem, Integer> itemCount = new HashMap<>();
        StringBuilder summary = new StringBuilder("ALL ORDERS SUMMARY:\n");

        for(List<Order> orders : orderManager.getAllOrder()) {
            for(Order order : orders) {

                for(Map.Entry<MenuItem, Integer> entry : order.getOrder().entrySet()) {
                    if(itemCount.containsKey(entry.getKey())) {
                        itemCount.put(entry.getKey(), itemCount.get(entry.getKey()) + entry.getValue());
                    }
                    else {
                        itemCount.put(entry.getKey(), entry.getValue());
                    }
                }
                totalIncome += order.getPrize();
                totalDiscount += order.getTotalDiscount();
            }
        }
        summary.append("total income: $").append(String.format("%.2f", totalIncome)).append("\n");
        summary.append("total discount: $").append(String.format("%.2f", totalDiscount)).append("\n");
        for(MenuItem item : shopList.values()) {
            if(!itemCount.containsKey(item)) {
                itemCount.put(item, 0);
            }
        }
        for(Map.Entry<MenuItem, Integer> entry : itemCount.entrySet()) {
            summary.append(entry.getKey().getName()).append(" x ").append(entry.getValue()).append("\n");
        }


        summaryText.setText(summary.toString());
    }





    public static void main(String[] args)  {

//        OrderManager orderManager = new OrderManager();

        SwingUtilities.invokeLater(ShopGUI::new);

    }
}

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath, JPanel contentPanel) {
        backgroundImage = new ImageIcon(imagePath).getImage();
        setLayout(new GridLayout());
        contentPanel.setOpaque(false); // 让内容透明，显示背景
        add(contentPanel, BorderLayout.CENTER);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
//        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create(); // 创建 Graphics2D 对象，避免影响其他绘图
            float alpha = 0.3f; // 设置透明度（0.0 = 完全透明，1.0 = 不透明）
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose(); // 释放资源
        }
    }
}