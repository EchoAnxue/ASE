import javax.swing.*;
import java.util.HashMap;

public class Cook implements Runnable {

    private final JTextArea cookStatusLabel;
    private final Object lock; // 用于与 Server 通信
    private final String name;

    public Cook( String name, JTextArea cookStatusLabel, Object lock) {
        this.cookStatusLabel = cookStatusLabel;
        this.lock = lock;
        this.name = name;
    }

    @Override
    public void run() {
        cookStatusLabel.setText(name);
        while (true) {
            Order currentOrder;

            // 获取订单（加锁保证线程安全）
            synchronized (CookOrderManager.getOrderList()) {
//                while (CookOrderManager.getOrderList()==null) {
//                    try {
//                        CookOrderManager.wait(); // 没订单就等待
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                currentOrder = CookOrderManager.getOrder(); // 获取并移除头部订单
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

            // === 模拟烹饪 ===
            StringBuilder sb = new StringBuilder();
            sb.append("\nProcessing ID =").append(currentOrder.getID()).append(" 's order.\n");
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


            // 模拟制作耗时
            try {
                cookStatusLabel.append("-- COOK DONE --");
                ServerOrderManager.addOrder(currentOrder);
                Logger.getInstance().log(name + " finishes cooking Order" + currentOrder.getID() + ".\n");
                synchronized (lock) {
                    lock.notifyAll();
                }
                Thread.sleep(TimeManager.adjustTime(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }
}
