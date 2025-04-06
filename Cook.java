import javax.swing.*;
import java.util.HashMap;

public class Cook implements Runnable {

    private final JTextArea cookStatusLabel;
    private final Object lock; //  Server lock
    private final String name;
    private final OrderManager orderManager;

    public Cook( String name, JTextArea cookStatusLabel, Object lock, OrderManager orderManager) {
        this.cookStatusLabel = cookStatusLabel;
        this.lock = lock;
        this.name = name;
        this.orderManager = orderManager;
    }

    @Override
    public void run() {
        cookStatusLabel.setText(name);
        while (true) {
            Order currentOrder;

            // safe
            synchronized (CookOrderManager.getOrderList()) {

                currentOrder = CookOrderManager.getOrder(); // get and delete head order
                // empty
                if (currentOrder==null){
                    ServerOrderManager.addOrder(Order.createPoisonPill());
                    break;
                }
                // sufficient
                CookOrderManager.finishOrder(currentOrder);
                cookStatusLabel.setText(name);

                Logger.getInstance().log(name + " gets an order from waiting to cook list.\n");
                Logger.getInstance().logOrder(currentOrder);
            }

            // === UI simulation ===
            StringBuilder sb = new StringBuilder();
            String customerName = orderManager.getCustomerByOrder(currentOrder.getID()).getName();
            sb.append("\nProcessing "+ customerName +" (ID =").append(currentOrder.getID()).append(" )'s order.\n");
            cookStatusLabel.append(sb.toString());
            for (HashMap.Entry<MenuItem, Integer> entry : currentOrder.getOrder().entrySet()) {

                cookStatusLabel.append(entry.getValue()+"\t"+entry.getKey().getName()+"\n");
                try {
                    Logger.getInstance().log(name + " starts cooking Order" + currentOrder.getID() + ".\n");
                    Thread.sleep(TimeManager.adjustTime(1000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }


            // UI COOK DONE
            try {

                synchronized (lock) {
                    cookStatusLabel.append("-- COOK DONE --");
                    ServerOrderManager.addOrder(currentOrder);
                    Logger.getInstance().log(name + " finishes cooking Order" + currentOrder.getID() + ".\n");
                    lock.notifyAll();
                }
                Thread.sleep(TimeManager.adjustTime(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
