import javax.swing.*;
import java.util.HashMap;

public class Server implements Runnable {

    private final JTextArea ServerStatusTextArea;
    private final Object lock; // 用于与 Server 通信
    private final String name;
    private final OrderManager orderManager;


    public Server(String name, JTextArea cookStatusLabel, OrderManager orderManager, Object lock) {
        this.ServerStatusTextArea = cookStatusLabel;
        this.lock = lock;
        this.name = name;
        this.orderManager = orderManager;
    }

    @Override
    public void run() {
        ServerStatusTextArea.setText(name);

        while (true) {
            Order orderToServe;

            synchronized (lock) {


                orderToServe = ServerOrderManager.getOrder();


                while (orderToServe == null) {
                    try {
                        lock.wait(); // 等待 cook 通知
                        orderToServe = ServerOrderManager.getOrder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ServerOrderManager.finishOrder(orderToServe);


            }
            if (orderToServe.getTime().equals("poisonpill")) {
                ServerStatusTextArea.setText("\nDelivering FINISH");
                break;
            }

            // 模拟送餐

            ServerStatusTextArea.setText("\nDelivering order ID = " + orderToServe.getID());
            try {

                String customerName = orderManager.getCustomerByOrder(orderToServe.getID()).getName();
                int totalItemCount = orderToServe.getOrder().size();

                ServerStatusTextArea.append("\n Customer: \t" + customerName);
                Thread.sleep(1000);
//                item
                for (HashMap.Entry<MenuItem, Integer> entry : orderToServe.getOrder().entrySet()) {

                    ServerStatusTextArea.append("\n" + entry.getValue() + "\t" + entry.getKey().getName());
                    Thread.sleep(1000);


                }
                ServerStatusTextArea.append("\ntotal prize :\t"
                        + String.valueOf(orderToServe.getPrize()));
                ServerStatusTextArea.append("\ntotal discount :\t"
                        + String.valueOf(orderToServe.getTotalDiscount()));

                Thread.sleep(1000); // 模拟送餐时间
                GUIOrderManager.finishOrder(orderToServe.getID());
                ServerStatusTextArea.append("\n--- Delivered! ---");
                ServerOrderManager.addOrder(currentOrder);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
