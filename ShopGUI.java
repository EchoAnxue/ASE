
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

//
//    public synchronized int  incrementCustomerId() {
//        System.out.println("Before Inc " + currentCustomerId);
//        currentCustomerId++;
//        System.out.println("After Inc " + currentCustomerId);
//        return currentCustomerId;
//    }
//
//    public synchronized int decrementCurrentCustomerId() {
//        System.out.println("\\t\\t\\t Before Dec " + currentCustomerId);
//        currentCustomerId--;
//        System.out.println("\\t\\t\\t After Dec " + currentCustomerId);
//        return currentCustomerId;
//    }
//    public synchronized int incrementOrderId() {
//        System.out.println("\\t\\t\\t\\t Before Inc " + currentOrderId);
//        currentOrderId++;
//        System.out.println("\\t\\t\\t\\t After Inc " + currentOrderId);
//        return currentOrderId;
//    }
//
//    public synchronized int decrementCurrentOrderId() {
//        System.out.println("\\t\\t\\t\\t\\t Before Dec " + currentOrderId);
//        currentOrderId--;
//        System.out.println("\\t\\t\\t\\t\\t After Dec " + currentOrderId);
//        return currentOrderId;
//    }
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


        mainPanel.add(createMenuPanel(), "Menu");
        mainPanel.add(createOrderPanel(), "Order");
        mainPanel.add(createBillPanel(), "Bill");
        mainPanel.add(createSummaryPanel(), "Summary");


        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea menuText = new JTextArea("Menu:\n");
        for (MenuItem item : shopList.values()) {
            menuText.append(item.getName() + " - price:" + item.getCost() + "\n");
        }
        menuText.setEditable(false);
        panel.add(menuText, BorderLayout.CENTER);

        JButton nextButton = new JButton("GO TO ORDER");
        nextButton.addActionListener(e -> cardLayout.show(mainPanel, "Order"));
        panel.add(nextButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new GridLayout(shopList.size() + 3, 1));

        JPanel namePanel = new JPanel(new BorderLayout());
        customerNameField = new JTextField("please input your name");
        confirmNameButton = new JButton("确认");
        confirmNameButton.addActionListener(e -> toggleItemButtons());
        namePanel.add(customerNameField, BorderLayout.CENTER);
        namePanel.add(confirmNameButton, BorderLayout.EAST);
        panel.add(namePanel);

        for (MenuItem item : shopList.values()) {
            JPanel itemPanel = new JPanel(new FlowLayout());
//            display
            JTextField quantityField = new JTextField("0", 3);
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
//            - item + amount

            panel.add(itemPanel);
        }


//
//            JButton button = new JButton(item.name + " ($" + item.price + ")");
//            button.addActionListener(e -> addToOrder(item, quantityField));
//            button.setEnabled(false);
//            itemButtons.add(button);
//

//
//            panel.add(itemPanel);
        JButton checkoutButton = new JButton("查看订单");
        checkoutButton.addActionListener(e -> {
            customerName = customerNameField.getText().trim();
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "请先输入您的姓名！");
            } else {
                updateBill();
                cardLayout.show(mainPanel, "Bill");
            }
        });
        panel.add(checkoutButton);

        return panel;
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
        customerNameField.setEditable(false);
    }
    private JPanel createBillPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        orderBill = new JTextArea();
        orderBill.setEditable(false);
        panel.add(new JScrollPane(orderBill), BorderLayout.CENTER);

        JButton backButton = new JButton("See Summary");
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Summary"));
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }



    private void updateBill() {
        StringBuilder Bill = new StringBuilder("订单摘要:\n");
        for (MenuItem item : billList.keySet()) {
            Bill.append(item.getName()).append(" x ").append(billList.get(item)).append("\n");
        }
        DataLoader.writeACustomer(new Customer(currentCustomerId, customerName));
        Order order = new Order(currentOrderId, billList,currentCustomerId, LocalDateTime.now().toString(),
                false, 0.0F,false,0, 0);
        order.calculatePrize();
//        TODO: write to file
        DataLoader.writeOrder(order);
//
        orderManager.addOrder(order);


        Bill.append("总价: $").append(String.format("%.2f", order.getPrize())).append("\n");
        orderBill.setText(Bill.toString());
    }


    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        summaryText = new JTextArea("Summary:\n");
        summaryText.setEditable(false);
        displaySummary();
//        TODO: display summary

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
        HashMap<MenuItem, Integer> itemCount = new HashMap<>();
        StringBuilder summary = new StringBuilder("订单摘要:\n");

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
            }
        }
        summary.append("total income: $").append(String.format("%.2f", totalIncome)).append("\n");
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