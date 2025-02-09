import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DataLoaderTest {
    @BeforeEach
    void setUp() {
        DataLoader dataLoader = new DataLoader();
    }

    @Test
    public void TestwriteOrder() throws IOException {
        Customer customer = new Customer(2244,"anxue");

        Order order = new Order(2,customer);
        MenuItem menuItem = new MenuItem("fgfg","beverage",
                3.4f,"FSF335","no");
        order.addItem(menuItem,2);

        DataLoader dataLoader = new DataLoader();
        dataLoader.writeOrder(order);
    }
    @Test
    public void TestreadOrder() throws IOException {

        DataLoader dataLoader = new DataLoader();

        dataLoader.loadOrder();
        for(Order order1 : dataLoader.getOrderList().values()) {
            System.out.println(order1.toString());
        }
    }
    @Test
    public void TestLoadMenu() throws IOException {
        DataLoader dataLoader = new DataLoader();

        System.out.println(dataLoader.loadMenu());
    }
}