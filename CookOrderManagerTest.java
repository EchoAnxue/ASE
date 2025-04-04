import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class CookOrderManagerTest {

    @Test
    public void testAddNullOrder_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> CookOrderManager.addOrder(null));
    }

    @Test
    public void testFinishNonExistentOrder_ThrowsException() {
        Customer customer1 = new Customer(1, "John Wick",1);
        Order order = new Order(1, customer1);
        assertThrows(NoSuchElementException.class, () -> CookOrderManager.finishOrder(order));
    }

    @Test
    public void testNormalFlow() {
        Customer customer1 = new Customer(1, "John Wick",1);
        Customer customer2 = new Customer(2, "Adam Smith",1);
        Order order1 = new Order(1, customer1);
        Order order2 = new Order(2, customer2);

        CookOrderManager.addOrder(order1);
        CookOrderManager.addOrder(order2);
        assertEquals(order1, CookOrderManager.getOrder());

        // fulfill orders and verify queue updates
        CookOrderManager.finishOrder(order1);
        assertEquals(order2, CookOrderManager.getOrder());

        // the queue should be empty after the last order is completed
        CookOrderManager.finishOrder(order2);
        assertNull(CookOrderManager.getOrder());
    }

    @Test
    public void testFinishSameOrderTwice_ThrowsException() {
        Customer customer1 = new Customer(1, "John Wick",1);
        Order order = new Order(1, customer1);
        CookOrderManager.addOrder(order);
        CookOrderManager.finishOrder(order);
        assertThrows(NoSuchElementException.class, () -> CookOrderManager.finishOrder(order));
    }
}