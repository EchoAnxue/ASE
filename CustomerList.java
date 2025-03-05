import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a list of customers.
 * @author <Suntanqing FU> <sf4009@hw.ac.uk>
 * @version 0.01
 * @since 2025-02-07
 * 
 * **** Attributes ****
 * @custoList:static, List<Customer>, a list of customers
 * 
 * **** Methods ****
 * @addCustomerByName(String name):static, add a customer to the list, automatically generate a unique ID for the customer
 * parameters: name of the customer
 * return: Void
 * @editCustomer(int custoID, String name):static, edit the name of a customer already in the list
 * parameters: ID of the customer to be edited, new name of the customer
 * return: Void
 * @getCustomerByID(int custoID):static, get a customer from the list by ID
 * parameters: ID of the customer to be retrieved
 * return: Customer
 * @getIndex(int custoID):static, get the index of a customer in the list
 * parameters: index of the customer to be retrieved
 * return: int
 * @getCustoList():static, get the list of customers
 * parameters: void
 * return: List<Customer>
 * 
 */
public class CustomerList {

    private static ArrayList<Customer> custoList;

    public  CustomerList() {
        custoList = new ArrayList<Customer>();
    }

    // this method is used to add a customer to the list, automatically generate an unique ID for the customer
    public static void addCustomerByName(String name) throws AlreadyExistException {
        if(name == null || name.isEmpty())
            throw new IllegalArgumentException("Name cannot be null or empty");
        for (Customer custo : custoList) {
            if (custo.getName().equals(name))
                throw new AlreadyExistException("Customer " + name);
        }
        int custoID = custoList.size();
        Customer custo = new Customer(custoID, name);
        custoList.add(custo);
    }   

    // this method is used to reset the name of a customer already in the list
    public static void editCustomer(int custoID, String name) throws NotFoundException {
        int index = getIndex(custoID);
        if (index == -1)
            throw new NotFoundException("Customer " + custoID);
        Customer custo = custoList.get(index);
        custo.setName(name);
    }

    //this method is used to get a customer from the list by ID
    public static Customer getCustomerByID(int custoID){
        for (Customer custo : custoList) 
            if (custo.getID() == custoID)
                return custo;
        return null;
    }

    // this method is used to get the index of a customer in the list
    private static int getIndex(int custoID) {
        for (int i = 0; i < custoList.size(); i++) {
            if (custoList.get(i).getID() == custoID)
                return i;
        }
        return -1;
    }

    // this method is used to get the index of a customer in the list
    public static List<Customer> getCustoList() {
        return custoList;
    }

    public static void saveToFile(String fileName) throws IOException {
        //implement save to file
        DataLoader.loadCustomersList();

    }

    public static void loadFromFile(String fileName) {
        //implement load from file

            DataLoader.loadCustomersList();

    }

}
