/**
 * This class is used to generate sales reports based on the orders managed by OrderManager.
 * It calculates the total income and the count of each menu item ordered.
 * @author <Yang Yang> <yy2039@hw.ac.uk>
 * @version 0.01
 * @since 2025-02-12
 * 
 * ****Attributes****:
 * @itemOrderCount: a Map to store the count of each menu item ordered
 * @totalIncome: a float to store the total income from all orders
 * 
 * ****Methods****:
 * @countInformation(OrderManager orderManager): This method processes the orders from OrderManager to count each menu item and calculate total income.
 * @printReport(): This method prints the sales report, including the count of each menu item and the total income.
 * @getOrderCount(): This method returns the map containing the count of each menu item ordered.
 * @getTotalIncome(): This method returns the total income from all orders.
 */



import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ReportGenerator {

    private Map<MenuItem, Integer> itemOrderCount = new HashMap<>();
    private float totalIncome = 0;
    
    public void countInformation(OrderManager orderManager) {
        itemOrderCount.clear();
        totalIncome = 0;
        List<List<Order>> finalOrderList = orderManager.orderList;
        
        for (List<Order> orders : finalOrderList) {
            for (Order order : orders) {
                // add up order prize
                totalIncome += order.getPrize();
                // add up number of menuItem
                Map<MenuItem, Integer> itemsOrdered = order.getOrder();
                for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
                    MenuItem item = entry.getKey();
                    int quantity = entry.getValue();
                    itemOrderCount.put(item, itemOrderCount.getOrDefault(item, 0) + quantity);
                }
            }
        }
    }
    
    public void printReport() {
        System.out.println("Sales Report:");
        for (Map.Entry<MenuItem, Integer> entry : itemOrderCount.entrySet()) {
            System.out.println(entry.getKey().getName() + ": " + entry.getValue());
        }
        System.out.println("Total Income: " + totalIncome);
    }
    
    public Map<MenuItem, Integer> getOrderCount() {
        return itemOrderCount;
    }
    
    public float getTotalIncome() {
        return totalIncome;
    }
}