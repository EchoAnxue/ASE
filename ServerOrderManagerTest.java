import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ServerOrderManagerTest {

    @Test
    public void testAddNullOrder_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> ServerOrderManager.addOrder(null));
    }

    @Test
    public void testFinishNonExistentOrder_ThrowsException() {
        Customer customer1 = new Customer(1, "John Wick",1);
        Order order = new Order(1, customer1);
        assertThrows(NoSuchElementException.class, () -> ServerOrderManager.finishOrder(order));
    }

    @Test
    public void testNormalFlow() {
        Customer customer1 = new Customer(1, "John Wick",1);
        Customer customer2 = new Customer(2, "Adam Smith",1);
        Order order1 = new Order(1, customer1);
        Order order2 = new Order(2, customer2);

        ServerOrderManager.addOrder(order1);
        ServerOrderManager.addOrder(order2);
        assertEquals(order1, ServerOrderManager.getOrder());

        // fulfill orders and verify queue updates
        ServerOrderManager.finishOrder(order1);
        assertEquals(order2, ServerOrderManager.getOrder());

        // the queue should be empty after the last order is completed
        ServerOrderManager.finishOrder(order2);
        assertNull(ServerOrderManager.getOrder());
    }

    @Test
    public void testFinishSameOrderTwice_ThrowsException() {
        Customer customer1 = new Customer(1, "John Wick",1);
        Order order = new Order(1, customer1);
        ServerOrderManager.addOrder(order);
        ServerOrderManager.finishOrder(order);
        assertThrows(NoSuchElementException.class, () -> ServerOrderManager.finishOrder(order));
    }
}