import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class CookOrderManager {
    private static Queue<Order> orderList = new LinkedList<>();

    /**
     * add an order to the cook's list
     */
    public static void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        orderList.add(order);
    }

    /**
     * show the next order to cook
     */
    public static Order getOrder() {
        return orderList.peek(); // 允许返回null表示队列为空
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