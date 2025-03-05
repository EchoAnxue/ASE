import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class DataLoaderTest {
//    @BeforeEach
//    void setUp() {
//        DataLoader dataLoader = new DataLoader();
//    }

    @Test
    public void TestwriteOrder() throws IOException, GenerateException {
        Customer customer = new Customer(1,"anxue");

        Order order = new Order(2,customer);
        MenuItem menuItem = new MenuItem("fgfg","beverage",
                3.4f,"FOOD4","no");
        order.addItem(menuItem,2);


        DataLoader.writeOrder(order);
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

        System.out.println(DataLoader.loadMenu());
    }

    @Test
    public void TestLoadCustomers() throws IOException {


        System.out.println(DataLoader.loadCustomersList());
    }

    @Test
    public void TestWriteCustomers() throws IOException {

        ArrayList<Customer> customerList = new ArrayList<>();
        customerList.add(new Customer(1,"anxue"));
        customerList.add(new Customer(2,"liuzhen"));
        DataLoader.writeCustomersList(customerList);
        }
        @Test
//
        public void TestWriteACustomer() throws IOException {
        DataLoader.loadCustomersList();
        ArrayList<Customer> customerList = new ArrayList<>();

        customerList.add(new Customer(0,"Li hong"));

        DataLoader.writeACustomer(customerList.get(0));
        }

}