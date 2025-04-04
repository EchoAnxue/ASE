import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class GUIOrderManagerTest {

    @Test
    public void testNormalFlow() {
        GUIOrderManager manager = new GUIOrderManager();

        Customer customer1 = new Customer(1, "John Wick",1);
        Customer customer2 = new Customer(2, "Adam Smith",1);
        Order order1 = new Order(1, customer1);
        Order order2 = new Order(2, customer2);

        // test addOrder and GetOrder
        manager.addOrder(order1);
        manager.addOrder(order2);
        assertEquals(order1, manager.getOrder());

        // test finishOrder
        manager.finishOrder(1);
        assertEquals(order2, manager.getOrder());

        // test of non-existent orders (throw exception)
        assertThrows(NoSuchElementException.class, () -> manager.finishOrder(1));
    }

    @Test
    public void testFinishOrder_InvalidID() {
        GUIOrderManager manager = new GUIOrderManager();
        assertThrows(NoSuchElementException.class, () -> manager.finishOrder(999));
    }
}