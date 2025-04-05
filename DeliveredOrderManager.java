import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveredOrderManager {
    private static final List<Order> deliveredOrders = Collections.synchronizedList(new ArrayList<>());
//    private static final List<Order> deliveredOrders = new ArrayList<>();

    public static void addDeliveredOrder(Order order) {
        if (order != null) {
            deliveredOrders.add(order);
        }
    }

    public static List<Order> getDeliveredOrders() {
        return new ArrayList<>(deliveredOrders);
    }
}