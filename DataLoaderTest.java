import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DataLoaderTest {
//    @BeforeEach
//    void setUp() {
//        DataLoader dataLoader = new DataLoader();
//    }

    @Test
    public void TestwriteOrder() throws IOException {
        Customer customer = new Customer(1,"anxue");

        Order order = new Order(2,customer);
        MenuItem menuItem = new MenuItem("fgfg","beverage",
                3.4f,"FOOD4","no");
        order.addItem(menuItem,2);

        DataLoader dataLoader = new DataLoader();
        dataLoader.writeOrder(order);
    }
    @Test
    public void TestreadOrder() throws IOException {
        DataLoader.loadMenu();

        DataLoader.loadOrder();
        DataLoader.getOrderList();
        //return DataLoader.OrderList;

    }
    @Test
    public void TestLoadMenu() throws IOException {
        DataLoader dataLoader = new DataLoader();

        System.out.println(dataLoader.loadMenu());
    }
}