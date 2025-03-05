import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class represents an order made by a customer.
 * @author <Suntanqing FU> <sf4009@hw.ac.uk>
 * @version 0.05
 * @since 2025-02-10
 * 
 * ****Attributes****:
 * @orderID: int, unique identifier for the order
 * @itemsOrdered: Map<MenuItem,Integer>, a map of menu items and their quantities ordered
 * @customerID: int, unique identifier for the customer who made the order
 * @time: String, timestamp indicating when the order was made
 * @payment: boolean, boolean value indicating if the order has been paid
 * @prize: float, the total price of the order after any discounts have been applied
 * @isRegularCustomer: boolean, boolean value indicating if the customer is a regular customer (orders amount > 7)
 * @totalDiscount: float, the total amount of any applicable discounts applied to the order
 * @originalPrice: float, the original price of the order before any discounts were applied
 * 
 * ****Methods****:
 * @setRegularCustomer: sets the regular customer status of the order
 * parameters: isRegularCustomer
 * return: void
 * @addItem: adds an item to the itemsOrdered HashMap
 * parameters: item, quantity
 * return: void
 * @getPaymentStatus: returns the payment status of the order
 * parameters: none
 * return: boolean
 * @setPaymentStatus: sets the payment status of the order to true
 * parameters: none
 * return: void
 * @calculateTotalPrice: calculates the total price of the order by multiplying the cost of each item by its quantity
 * parameters: none
 * return: float
 * @calculateDiscount: applies any applicable discounts to the order
 * parameters: none
 * return: void
 * @getID: returns the orderID
 * parameters: none
 * return: int
 * @getOrder: returns the itemsOrdered HashMap
 * parameters: none
 * return: HashMap<MenuItem, Integer>
 * @getCustoID: returns the customerID
 * parameters: none
 * return: int
 * @getTime: returns the timestamp
 * parameters: none
 * return: String
 * @toString: returns a string representation
 * parameters: none
 * return: String
 * @isRegularCustomer: returns the regular customer status
 * parameters: none
 * return: boolean
 * @getTotalDiscount: returns the total discount applied to the order
 * parameters: none
 * return: float
 * @getOriginalPrice: returns the original price of the order
 * parameters: none
 * return: float
 */
public class Order {
    private int orderID;
    private Map<MenuItem, Integer> itemsOrdered;
    private int customerID;
    private String time;
    private boolean payment;
    private Float prize;
    private boolean isRegularCustomer;
    private float totalDiscount;
    private float originalPrice;

    /** Constructor for creating a new order
    * parameters: orderID, customer
    */
    public Order(int orderID, Customer customer) {
        this.orderID = orderID;
        this.itemsOrdered = new HashMap<>();
        this.customerID = customer.getID();
        this.time = LocalDateTime.now().toString();
        this.payment = false;
        this.prize = 0.0f;
        this.isRegularCustomer = false;
        this.totalDiscount = 0.0f;
        this.originalPrice = 0.0f;
    }

    public Order(int orderID, Map<MenuItem, Integer> itemsOrdered, int customerID,
                 String time, boolean payment, Float prize, boolean isRegularCustomer,
                 float totalDiscount, float originalPrice) {
        this.orderID = orderID;
        this.itemsOrdered = itemsOrdered;
        this.customerID = customerID;
        this.time = time;
        this.payment = payment;
        this.prize = prize;
        this.isRegularCustomer = isRegularCustomer;
        this.totalDiscount = totalDiscount;
        this.originalPrice = originalPrice;
    }

    // Method to set the regular customer status
    // parameter: isRegularCustomer
    public void setRegularCustomer(boolean isRegularCustomer) {
        this.isRegularCustomer = isRegularCustomer;
    }

    // Method to add an item to the order
    // parameters: item, quantity
    public void addItem(MenuItem item, int quantity) {
        itemsOrdered.put(item, quantity);
    }

    // Method to set the payment status to true
    public void setPaymentStatus() {
        payment = true;
    }

    // Method to calculate the total price of the order
    // return: the total price of the order
    public float calculateTotalPrice() {
        float total = 0;
        for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
            total += entry.getKey().getCost() * entry.getValue();
        }
        return total;
    }

    // Method to apply any applicable discounts to the order
    // return: the total price of the order after discounts have been applied
    public void calculatePrize() {

        originalPrice = calculateTotalPrice();

        for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
            if (entry.getKey().getCategory().toLowerCase().equals("beverage")) {
                if (entry.getValue() > 1) {
                    totalDiscount += entry.getValue() / 2 * 0.40 * entry.getKey().getCost();
                    System.out.println("Half off for beverages with more than 1 item"+totalDiscount);
                }
            }
            if (entry.getKey().getCategory().equals("food")) {
                if (entry.getValue() > 2) {
                    totalDiscount += entry.getValue() / 3 * 0.50 * entry.getKey().getCost();
                    System.out.println("Half off for food with more than 2 item"+totalDiscount);
                }
            }
        }

        if (isRegularCustomer) {
            totalDiscount = originalPrice - (originalPrice - totalDiscount) * 0.95f;
            System.out.println("0.95 off for old customers"+totalDiscount);
        }
        prize = originalPrice - totalDiscount;
    }

    public int getID() {
        return orderID;
    }

    public Map<MenuItem, Integer> getOrder() {
        return itemsOrdered;
    }

    public int getCustoID() {
        return customerID;
    }

    public String getTime() {
        return time;
    }

    public boolean getPaymentStatus() {
        return payment;
    }

    public float getPrize() {

        return prize;
    }

    // toString method to print the order details
    // return: a string representation of the order
    @Override
    public String toString() {
        return "Order{id=" + orderID + ", customer=" + customerID + ", items=" + itemsOrdered.size() + "}";
    }

    public boolean isRegularCustomer() {
        return isRegularCustomer;
    }

    public float getTotalDiscount() {
//        add function otherwise it will return 0
        this.calculatePrize();
        return totalDiscount;
    }

    public float getOriginalPrice() {
        calculateTotalPrice();
        return originalPrice;
    }
}
