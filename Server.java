import javax.swing.*;
import java.util.HashMap;

public class Server implements Runnable {

    private final JTextArea ServerStatusTextArea;
    private final Object lock; // lock safety
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
                        lock.wait(); // sleep
                        orderToServe = ServerOrderManager.getOrder();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ServerOrderManager.finishOrder(orderToServe);

                Logger.getInstance().log(name + " gets an order from waiting to serve list.\n");
                Logger.getInstance().logOrder(orderToServe);


            }
            if (orderToServe.getTime().equals("poisonpill")) {

                ServerStatusTextArea.setText(name+"\nDelivering FINISH");
                break;
            }

            // =============  simulation============
            ServerStatusTextArea.setText(name);

            try {
                Logger.getInstance().log(name + " starts serving Order" + orderToServe.getID() + ".\n");

                String customerName = orderManager.getCustomerByOrder(orderToServe.getID()).getName();
                ServerStatusTextArea.append("\nDelivering order "+customerName+"( ID = " + orderToServe.getID()+" ) ");
                int totalItemCount = orderToServe.getOrder().size();

                ServerStatusTextArea.append("\n Customer: \t" + customerName);
                Thread.sleep(TimeManager.adjustTime(1000));
//                item
                for (HashMap.Entry<MenuItem, Integer> entry : orderToServe.getOrder().entrySet()) {

                    ServerStatusTextArea.append("\n" + entry.getValue() + "\t" + entry.getKey().getName());
                    Thread.sleep(TimeManager.adjustTime(1000));


                }
                ServerStatusTextArea.append("\ntotal prize :\t"
                        + String.valueOf(orderToServe.getPrize()));
                ServerStatusTextArea.append("\ntotal discount :\t"
                        + String.valueOf(orderToServe.getTotalDiscount()));

                Thread.sleep(TimeManager.adjustTime(1000)); // time
                GUIOrderManager.finishOrder(orderToServe.getID());
                ServerStatusTextArea.append("\n--- Delivered! ---");
                DeliveredOrderManager.addDeliveredOrder(orderToServe);
                Logger.getInstance().log(name + " finishes serving Order" + orderToServe.getID() + ".\n");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
