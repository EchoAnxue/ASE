import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer(1, "John Wick");
    }

    @Test
    public void testGetID() {
        // Test the getID() method
        assertEquals(1, customer.getID());
    }

    @Test
    public void testToString() {
        // Test the toString() method
        String expected = "id= 1 , name= John Wick ";
        assertEquals(expected, customer.toString());
    }

    @Test
    public void testGetName() {
        assertEquals("John Wick", customer.getName());
    }

    @Test
    public void testSetName() {
        customer.setName("John Smith");
        assertEquals("John Smith", customer.getName());
    }

}
