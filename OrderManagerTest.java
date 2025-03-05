import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class OrderManagerTest {
    private OrderManager orderManager;
    private Customer testCustomer;
    private Order testOrder;
    private MenuItem menuItem;

    @BeforeEach
    void setUp() throws GenerateException {
        orderManager = new OrderManager();

        int customerID = orderManager.incrementCustomerId();
        testCustomer = new Customer(customerID, "John Wick");

        menuItem = new MenuItem("Burger", "food", 5.99f, "FOD1905", "description");

        Map<MenuItem, Integer> itemsOrdered = new HashMap<>();
        itemsOrdered.put(menuItem, 2);  // 订单包含 2 份 Burger

        int orderID = orderManager.incrementOrderId();
        testOrder = new Order(orderID, itemsOrdered, customerID,
                "12:30 PM", false, 11.98f,
                false, 0.0f, 11.98f);
    }

    @Test
    void testAddOrder() {
        orderManager.addCustomer(testCustomer.getID());
        orderManager.addOrder(testOrder);

        List<Order> orders = orderManager.getOrderByCusto(testCustomer);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
        assertEquals(testOrder.getID(), orders.get(0).getID());
    }

    @Test
    void testRemoveOrder() throws NotFoundException {
        orderManager.addCustomer(testCustomer.getID());
        orderManager.addOrder(testOrder);

        // make sure order exist
        assertNotNull(orderManager.getOrderByOrderID(testOrder.getID()));

        // remove order
        orderManager.removeOrder(testOrder.getID());

        // make sure order removed
        assertNull(orderManager.getOrderByOrderID(testOrder.getID()));
    }

    @Test
    void testAddCustomer() {
        orderManager.addCustomer(testCustomer.getID());

        // make sure customer added
        assertDoesNotThrow(() -> orderManager.getOrderByCusto(testCustomer.getID()));
    }

    @Test
    void testGetOrderByOrderID() {
        orderManager.addCustomer(testCustomer.getID());
        orderManager.addOrder(testOrder);

        Order fetchedOrder = orderManager.getOrderByOrderID(testOrder.getID());
        assertNotNull(fetchedOrder);
        assertEquals(testOrder.getID(), fetchedOrder.getID());
    }

    @Test
    void testGetOrderByCustomerID() throws NotFoundException {
        orderManager.addCustomer(testCustomer.getID());
        orderManager.addOrder(testOrder);

        List<Order> orders = orderManager.getOrderByCusto(testCustomer.getID());
        assertFalse(orders.isEmpty());
        assertEquals(testOrder.getID(), orders.get(0).getID());
    }

    @Test
    void testGetOrderCount() {
        orderManager.addCustomer(testCustomer.getID());
        orderManager.addOrder(testOrder);

        int count = orderManager.getOrderCount(testCustomer.getID());
        assertEquals(1, count);
    }
}
