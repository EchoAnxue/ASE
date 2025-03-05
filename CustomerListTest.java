import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CustomerListTest {

    private CustomerList customerList;

    @BeforeEach
    public void setUp() {
        customerList = new CustomerList(); // Create a new instance before each test
    }

    @Test
    public void testAddCustomerByName() throws AlreadyExistException {
        customerList.addCustomerByName("John Wick");
        List<Customer> customers = customerList.getCustoList();
        assertEquals(1, customers.size());
        assertEquals("John Wick", customers.get(0).getName());
    }

    @Test
    public void testAddDuplicateCustomer() {
        assertThrows(AlreadyExistException.class, () -> {
            customerList.addCustomerByName("John Wick");
            customerList.addCustomerByName("John Wick"); // This should throw an exception
        });
    }

    @Test
    public void testEditCustomer() throws AlreadyExistException, NotFoundException {
        // Test editing a customer's name
        customerList.addCustomerByName("John Wick");
        Customer customer = customerList.getCustomerByID(0);
        assertNotNull(customer);

        customerList.editCustomer(0, "John Smith");
        assertEquals("John Smith", customer.getName());
    }

    @Test
    public void testEditNonExistingCustomer() {
        assertThrows(NotFoundException.class, () -> {
            customerList.editCustomer(999, "Fake Customer"); // Non-existent ID
        });
    }

    @Test
    public void testGetCustomerByID() throws AlreadyExistException {
        customerList.addCustomerByName("John Wick");
        Customer customer = customerList.getCustomerByID(0);
        assertNotNull(customer);
        assertEquals("John Wick", customer.getName());
    }

    @Test
    public void testGetCustoList() throws AlreadyExistException {
        customerList.addCustomerByName("John Wick");
        customerList.addCustomerByName("John Smith");

        List<Customer> customers = customerList.getCustoList();
        assertEquals(2, customers.size());
    }

    @Test
    public void testAddCustomerWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerList.addCustomerByName(null);
        });
    }

    @Test
    public void testAddCustomerWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerList.addCustomerByName("");
        });
    }
}
