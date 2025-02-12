import java.util.ArrayList;
import java.util.List;


/**
 * This class is used to manage the orders and customers.
 * @author <Suntanqing FU> <sf4009@hw.ac.uk>
 * @version 0.03
 * @since 2025-02-12
 *
 * ****Attributes****:
 * @orderList: a List to store the order list of each customer
 * @customerOrderCount: a List to store the order count of each customer
 *
 * ****Methods****:
 * @addOrder(Order order): This method is used to add an order to the order list, update the customer order count.
 * @removeOrder(int orderID): This method is used to remove an order from the order list, update the customer order count.
 * @addCustomer(Customer customer): This method is used to add a new customer to the order list.
 * @getCustomerByOrder(int orderID): This method is used to get the customer by using the order.
 * @getOrderByOrderID(int id): This method is used to get the order by using the order ID.
 * @getOrderByCusto(Customer customer): This method is used to get the order list of a customer by using the customer object.
 * @getOrderByCusto(int custoID): This method is used to get the order list of a customer by using the customer ID.
 * @getOrderCount(int custoID): This method is used to get the order count of a customer by using the customer ID.
 * @printCustomers():This method is used to print the list of all customers.
 */
public class OrderManager {
    // index is CustomerID
    private List<List<Order>> orderList;
    // index is CustomerID, int is order count
    private List<Integer> customerOrderCount;

    public OrderManager() {
        this.orderList = new ArrayList<List<Order>>();
        this.customerOrderCount = new ArrayList<Integer>();
    }

    // This method is added to add an order to the order list, update the customer order count and update the report
    public void addOrder(Order order) throws NotFoundException, AlreadyExistException {
        int custoID = order.getCustoID();
        if (orderList.size() > custoID && custoID >= 0) {
            if(getOrderCount(custoID) > 7)
                order.setRegularCustomer(true);
            orderList.get(custoID).add(order);
            customerOrderCount.set(custoID, customerOrderCount.get(custoID) + 1);
        } else {
            throw new NotFoundException("Customer ID "+order.getCustoID());
        }
    }

    // This method is added to remove an order from the order list, update the customer order count and update the report
    public void removeOrder(int orderID) throws NotFoundException {
        for (int custoID = 0; custoID < orderList.size(); custoID++) { // custoID = index
            for (int i = 0; i < orderList.get(custoID).size(); i++) { // i = index of order of one customer
                if (orderList.get(custoID).get(i).getID() == orderID) {
                    orderList.get(custoID).remove(i);
                    customerOrderCount.set(custoID, customerOrderCount.get(custoID) - 1);
                    System.out.println("Order "+orderID+" is removed.");
                    return;
                }
            }
        }
        throw new NotFoundException("Order "+orderID);
    }

    // This method is added to add new customer to the order list.
    public void addCustomer(Customer customer) {
        int custoID = customer.getID();
        if (orderList.size() > custoID && custoID >= 0) {
            System.out.println("Customer "+custoID+" already exist.");
        } else {
            orderList.add(new ArrayList<Order>());
            customerOrderCount.add(0);
            System.out.println("Customer "+custoID+" is added.");
        }
    }

    // This method is added to get the customer by using the order
    public Customer getCustomerByOrder(int orderID) {
        for (List<Order> orders : orderList)
            for (Order order : orders)
                if (order.getID() == orderID)
                    return CustomerList.getCustomerByID(order.getCustoID());
        return null;
    }

    // This method is added to get the order by using the order ID
    public Order getOrderByOrderID(int id) {
        for (List<Order> orders : orderList) {
            for (Order order : orders) {
                if (order.getID() == id) {
                    return order;
                }
            }
        }
        return null;
    }

    // This method is added to get the order list of a customer by using the customer object
    public List<Order> getOrderByCusto(Customer customer) throws NotFoundException {
        int custoID = customer.getID();
        if(orderList.size() > custoID && custoID >= 0)
            return orderList.get(custoID);
        else
            throw new NotFoundException("Customer ID " + custoID + " ");
    }

    // This method is added to get the order list of a customer by using the customer ID
    public List<Order> getOrderByCusto(int custoID) throws NotFoundException {
        if(orderList.size() > custoID && custoID >= 0)
            return orderList.get(custoID);
        else
            throw new NotFoundException("Customer ID " + custoID + " ");
    }

    // This method is added to get the order count of a customer by using the customer ID
    public int getOrderCount(int custoID) {
        return customerOrderCount.get(custoID);
    }


    // This method is added to print the list of all customers
    public void printCustomers() {
        System.out.println("Customers: ");
        for (Customer custo : CustomerList.getCustoList())
            System.out.println(custo.toString() + "Order(s) = " + customerOrderCount.get(custo.getID()) + " orders");

    }

}