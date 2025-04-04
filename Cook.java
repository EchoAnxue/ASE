//public class Cook {
//    private final List<Order> orderQueue = new ArrayList<>();  // 等待处理的订单
//    private final List<Order> doneQueue = new ArrayList<>();   // 处理完成的订单
//    private final Object lock = new Object();
//    public void Cook(){
//        while (true) {
//            synchronized (lock) {
//                while (orderQueue.isEmpty()) { // 没有订单时等待
//                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
//                }
//                Order order = orderQueue.remove(0);
//                System.out.println(Thread.currentThread().getName() + " 正在烹饪订单 " + order.getId());
//
//                try { Thread.sleep(2000); } catch (InterruptedException e) {} // 模拟烹饪时间
//
//                doneQueue.add(order); // 订单完成
//                System.out.println(Thread.currentThread().getName() + " 完成订单 " + order.getId());
//
//                lock.notifyAll(); // 通知 Server 线程
//            }
//        }
//    }
//}
