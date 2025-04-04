//import java.util.LinkedList;
//import java.util.Queue;
//
//class Server implements Runnable {
//    private String name;
//    private OrderQueue orderQueue;
//
//    public Server(String name, OrderQueue orderQueue) {
//        this.name = name;
//        this.orderQueue = orderQueue;
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                System.out.println(name + " 等待订单...");
//                Order order = orderQueue.takeOrder();  // 这里会 wait()，直到有订单
//                System.out.println(name + " 正在处理订单：" + order);
//                Thread.sleep(2000);  // 模拟处理时间
//                System.out.println(name + " 订单完成：" + order);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
