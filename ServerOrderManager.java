import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ServerOrderManager {
    // List of orders waiting to be cooked
    private static Queue<Order> orderList = new LinkedList<>();

    /**
     * add an order to the server's list
     */
    public static void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        orderList.add(order);
    }

    /**
     * show the next order to serve
     */
    public static Order getOrder() {
        return orderList.peek();
    }

    /**
     * remove the order
     */
    public static void finishOrder(Order order) {
        if (!orderList.contains(order)) {
            throw new NoSuchElementException("Order not found in the queue: " + order);
        }
        orderList.remove(order);
    }



}