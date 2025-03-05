import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MenuItemTest {

    @Test(expected = GenerateException.class)
    public void testConstructor_NameIsNull() throws GenerateException {
        new MenuItem(null, "beverage", 10.0f, "id", "description");
    }

    @Test(expected = GenerateException.class)
    public void testConstructor_CategoryInvalid() throws GenerateException {
        new MenuItem("coca cola", "none", 10.0f, "id", "description");
    }

    @Test(expected = GenerateException.class)
    public void testConstructor_CostZero() throws GenerateException {
        new MenuItem("coca cola", "beverage", 0.0f, "id", "description");
    }

    @Test(expected = GenerateException.class)
    public void testConstructor_CostOver1000() throws GenerateException {
        new MenuItem("coca cola", "beverage", 1000.0f, "id", "description");
    }

    @Test(expected = GenerateException.class)
    public void testConstructor_IdentifierIsNull() throws GenerateException {
        new MenuItem("coca cola", "beverage", 10.0f, null, "description");
    }

    @Test(expected = GenerateException.class)
    public void testConstructor_DescriptionIsNull() throws GenerateException {
        new MenuItem("coca cola", "beverage", 10.0f, "id", null);
    }

    @Test
    public void testValidMenuItemCreation() throws GenerateException {
        MenuItem item = new MenuItem(
                "Coffee",
                "beverage",
                5.99f,
                "coffee_001",
                "A hot coffee"
        );

        assertEquals("Coffee", item.getName());
        assertEquals("beverage", item.getCategory().toLowerCase()); // 验证不区分大小写
        assertEquals(5.99f, item.getCost(), 0.001);
        assertEquals("coffee_001", item.getIdentifier());
        assertEquals("A hot coffee", item.getDescription());
    }
}