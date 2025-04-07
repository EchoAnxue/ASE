import javax.swing.*;
import java.util.HashMap;

public class Server implements Runnable {

    private final JTextArea ServerStatusTextArea;  // Text area to display server status
    private final Object lock;  // Lock for thread synchronization
    private final String name;  // Name of the server
    private final OrderManager orderManager;  // OrderManager to manage orders

    // Constructor to initialize the Server with necessary parameters
    public Server(String name, JTextArea cookStatusLabel, OrderManager orderManager, Object lock) {
        this.ServerStatusTextArea = cookStatusLabel;  // Initialize text area for status updates
        this.lock = lock;  // Initialize lock object for synchronization
        this.name = name;  // Initialize server name
        this.orderManager = orderManager;  // Initialize order manager
    }

    // The main method for the server thread to run
    @Override
    public void run() {
        ServerStatusTextArea.setText(name);  // Display the server's name in the text area

        while (true) {
            Order orderToServe;

            // Synchronize access to shared resources using lock
            synchronized (lock) {

                // Get the next order from the order manager
                orderToServe = ServerOrderManager.getOrder();

                // If no order is available, wait until notified
                while (orderToServe == null) {
                    try {
                        lock.wait();  // Sleep until a new order is available
                        orderToServe = ServerOrderManager.getOrder();  // Get the next available order
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Mark the order as finished in the order manager
                ServerOrderManager.finishOrder(orderToServe);

                // Log and update the status
                Logger.getInstance().log(name + " gets an order from waiting to serve list.\n");
                Logger.getInstance().logOrder(orderToServe);
            }

            // If the order is a "poisonpill", terminate the server thread
            if (orderToServe.getTime().equals("poisonpill")) {
                ServerStatusTextArea.setText(name + "\nDelivering FINISH");  // Indicate finishing
                break;  // Exit the loop and end the server thread
            }

            // Simulation of the server delivering the order
            ServerStatusTextArea.setText(name);  // Update server status

            try {
                // Log that the server starts serving the order
                Logger.getInstance().log(name + " starts serving Order" + orderToServe.getID() + ".\n");

                String customerName = orderManager.getCustomerByOrder(orderToServe.getID()).getName();  // Get customer name
                ServerStatusTextArea.append("\nDelivering order " + customerName + "( ID = " + orderToServe.getID() + " ) ");
                int totalItemCount = orderToServe.getOrder().size();  // Get the total number of items in the order

                // Display customer details
                ServerStatusTextArea.append("\n Customer: \t" + customerName);
                Thread.sleep(TimeManager.adjustTime(1000));  // Adjust and simulate delivery time

                // Iterate over the items in the order and display each item
                for (HashMap.Entry<MenuItem, Integer> entry : orderToServe.getOrder().entrySet()) {
                    ServerStatusTextArea.append("\n" + entry.getValue() + "\t" + entry.getKey().getName());
                    Thread.sleep(TimeManager.adjustTime(1000));  // Simulate serving time for each item
                }

                // Display total prize and discount for the order
                ServerStatusTextArea.append("\ntotal prize :\t" + String.valueOf(orderToServe.getPrize()));
                ServerStatusTextArea.append("\ntotal discount :\t" + String.valueOf(orderToServe.getTotalDiscount()));

                Thread.sleep(TimeManager.adjustTime(1000));  // Final pause before marking the order as delivered

                // Finish the order in the GUI and log the delivery
                GUIOrderManager.finishOrder(orderToServe.getID());
                ServerStatusTextArea.append("\n--- Delivered! ---");
                DeliveredOrderManager.addDeliveredOrder(orderToServe);  // Add the order to the delivered list
                Logger.getInstance().log(name + " finishes serving Order" + orderToServe.getID() + ".\n");

            } catch (InterruptedException e) {
                throw new RuntimeException(e);  // Handle thread interruption exception
            }
        }
    }
}
