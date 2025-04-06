import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoggerTest {
    private Logger logger;

    @Before
    public void setUp() {
        // reset the single instance
        Logger.getInstance().saveToFile(); // Empty log
    }

    // ---------------------- NOT NORMAL  ----------------------
    @Test
    public void testLogOrder_NullOrder_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Logger.getInstance().logOrder(null));
    }

    @Test
    public void testLogString_NullMessage_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Logger.getInstance().log((String) null));
    }

    @Test
    public void testLogReportGenerator_NullReport_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Logger.getInstance().log((ReportGenerator) null));
    }

    @Test
    public void testLogCustomerList_NullList_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Logger.getInstance().log((CustomerList) null));
    }

    @Test
    public void testLogMenu_NullMenu_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Logger.getInstance().log((Menu) null));
    }

    // ---------------------- Normal situation ----------------------
    @Test
    public void testLogOrder_Success() {
        // 模拟订单和菜单项
        Customer customer = new Customer(1, "John Wick",1);
        Order order = new Order(1, customer);
        MenuItem item = new MenuItem("Latte", "beverage", 3.5f, "B001", "Coffee");
        order.addItem(item, 2);

        // log example
        Logger.getInstance().logOrder(order);

        // assert
        assertEquals(1, Logger.getInstance().getLogEntries().size());
    }

    @Test
    public void testLogString_Success() {
        Logger.getInstance().log("Test message");
        assertTrue(Logger.getInstance().getLogEntries().contains("Test message"));
    }
}