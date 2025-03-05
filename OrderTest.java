import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;

class OrderTest {
    private Order order;
    private MenuItem item1;
    private MenuItem item2;
    private Customer customer;

    @BeforeEach
    void setUp() throws GenerateException {
        customer = new Customer(1, "John Wick");
        order = new Order(1001, customer);
        item1 = new MenuItem("Coffee", "beverage", 5.0f, "BEV2132", "description");
        item2 = new MenuItem("Burger", "food", 10.0f, "FOD9371", "description");
    }

    @Test
    void testAddItem() {
        order.addItem(item1, 2);
        order.addItem(item2, 3);
        Map<MenuItem, Integer> items = order.getOrder();
        assertEquals(2, items.get(item1));
        assertEquals(3, items.get(item2));
    }

    @Test
    void testCalculateTotalPrice() {
        order.addItem(item1, 2);
        order.addItem(item2, 3);
        assertEquals(40.0f, order.calculateTotalPrice(), 0.01);
    }

    @Test
    void testCalculatePrize() {
        order.addItem(item1, 2);
        order.addItem(item2, 3);
        order.calculatePrize();
        assertTrue(order.getPrize() < 40.0f);
    }

    @Test
    void testSetPaymentStatus() {
        assertFalse(order.getPaymentStatus());
        order.setPaymentStatus();
        assertTrue(order.getPaymentStatus());
    }

    @Test
    void testIsRegularCustomer() {
        assertFalse(order.isRegularCustomer());
        order.setRegularCustomer(true);
        assertTrue(order.isRegularCustomer());
    }

}