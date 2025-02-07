/**
 * This class represents a customer in the coffee shop system.
 * @author <Suntanqing FU> <sf4009@hw.ac.uk>
 * @version 0.01
 * @since 2025-02-07
 * 
 * ****Attributes****:
 * @custoID: int, the unique ID of the customer
 * @name: String, the name of the customer
 * @orderCount: int, the number of orders made by the customer
 * 
 * ****Methods****:
 * @getID(): returns the ID of the customer
 * parameters: none
 * return: int
 * @toString(): String, returns a string representation of the customer object
 * parameters: none
 * return: String
 * @getOrderCount(): int, returns the number of orders made by the customer
 * parameters: none
 * return: int
 * @orderCountRaise(): void, increments the order count by 1
 * parameters: none
 * return: void
 */
public class Customer {
    private int custoID;
    private String name;
    private int orderCount;
    
    public Customer(int custoID, String name) {
        this.custoID = custoID;
        this.name = name;
        this.orderCount = 0;
    }
    
    public int getID() { 
        return custoID; 
    }
    
    @Override
    public String toString() {
        return "Customer{id=" + custoID + ", name='" + name + "', orderCount=" + orderCount + "}";
    }
    
    public int getOrderCount() { 
        return orderCount; 
    }
    public void orderCountRaise() { 
        this.orderCount++; 
    }
}
