import java.util.ArrayList;
import java.util.List;

public class DeliveredOrderManager {
    private static final List<Order> deliveredOrders = new ArrayList<>();

    public static void addDeliveredOrder(Order order) {
        if (order != null) {
            synchronized (deliveredOrders) {
                deliveredOrders.add(order);
            }
        }
        else {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }

    public static List<Order> getDeliveredOrders() {
        synchronized (deliveredOrders) {
            return new ArrayList<>(deliveredOrders);
        }
    }
}