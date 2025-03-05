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
        // 初始化 OrderManager
        orderManager = new OrderManager();

        // 创建测试顾客
        int customerID = orderManager.incrementCustomerId();
        testCustomer = new Customer(customerID, "John Wick");

        // 创建测试菜单项
        menuItem = new MenuItem("Burger", "food", 5.99f, "FOD1905", "description");

        // 创建测试订单的商品列表
        Map<MenuItem, Integer> itemsOrdered = new HashMap<>();
        itemsOrdered.put(menuItem, 2);  // 订单包含 2 份 Burger

        // 创建测试订单
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

        // 确保订单存在
        assertNotNull(orderManager.getOrderByOrderID(testOrder.getID()));

        // 移除订单
        orderManager.removeOrder(testOrder.getID());

        // 确保订单被移除
        assertNull(orderManager.getOrderByOrderID(testOrder.getID()));
    }

    @Test
    void testAddCustomer() {
        orderManager.addCustomer(testCustomer.getID());

        // 确保顾客 ID 相关的订单列表已创建
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
