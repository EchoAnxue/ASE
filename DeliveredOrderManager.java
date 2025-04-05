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
    }

    public static List<Order> getDeliveredOrders() {
        synchronized (deliveredOrders) {
            return new ArrayList<>(deliveredOrders); // 返回副本避免外部修改
        }
    }
}