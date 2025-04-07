/**
 * This class is used to manage and log various operations within the system, such as changes in the report generator data,
 * customer list, and menu. Logs are stored in a list and can be saved to a file.
 * @author <Yang Yang> <yy2039@hw.ac.uk> <Bilawal Hassan ><bh3006@hw.ac.uk>
 * @version 0.02
 * @since 2025-02-12
 *
 * ****Attributes****:
 * @logEntries: a List to store log entries as strings
 * @LOG_FILE: a constant string representing the file path where logs will be saved
 *
 * ****Methods****:
 * @getInstance(): Returns the singleton instance of the Logger.
 * @logOrder(Order order): Logs the details of an order.
 * @log(ReportGenerator reportGenerator): Logs the total income from the report generator.
 * @log(CustomerList customerList): Logs the state of the customer list.
 * @log(Menu menu): Logs the state of the menu.
 * @log(String message): Logs a generic string message.
 * @saveToFile(): Saves all log entries to a file and clears the logEntries list to avoid duplication.
 * @getLogEntries(): Returns a copy of the log entries list (used for testing).
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logger {
    private List<String> logEntries;  // List to store all log entries
    private static final String LOG_FILE = "log.txt";  // Constant for the log file path
    private static volatile Logger instance;  // Singleton instance (thread-safe)

    // Private constructor to prevent external instantiation
    private Logger() {
        this.logEntries = new ArrayList<>();  // Initialize the list of log entries
    }

    /**
     * Gets the unique instance of Logger using double-checked locking.
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {  // First check (no synchronization needed)
            synchronized (Logger.class) {  // Synchronize only when instance is null
                if (instance == null) {  // Second check (ensures thread safety)
                    instance = new Logger();  // Create the instance if it does not exist
                }
            }
        }
        return instance;  // Return the unique Logger instance
    }

    /**
     * Logs details of a given Order.
     * @param order The order to log.
     */
    public void logOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");  // Ensure order is not null
        }

        order.getOriginalPrice();  // Access original price for logging purposes
        order.getTotalDiscount();  // Access total discount for logging purposes

        StringBuilder itemsDetails = new StringBuilder();  // StringBuilder for storing item details
        int totalItems = 0;  // Variable to track the total number of items in the order

        // Loop through the order and append item details to itemsDetails
        for (Map.Entry<MenuItem, Integer> entry : order.getOrder().entrySet()) {
            MenuItem item = entry.getKey();  // MenuItem object
            int quantity = entry.getValue();  // Quantity of the item ordered
            totalItems += quantity;  // Update total item count

            // Append item details to the string builder
            itemsDetails.append(String.format(
                    "\n  - %s [%s] x%d | %.2f each | Subtotal: %.2f",
                    item.getName(),
                    item.getCategory(),
                    quantity,
                    item.getCost(),
                    item.getCost() * quantity
            ));
        }

        String rawTime = order.getTime();  // Get raw time from the order
        String truncatedTime = rawTime.contains(".") ?
                rawTime.split("\\.")[0] : rawTime;  // Remove the fractional part of time if present

        // Create the log message for the order
        String logMessage = String.format(
                "Order: ID=%d, CustomerID=%d, Time=%s\n" +
                        "Items (%d):%s\n" +
                        "Original Price: %.2f | Total Discount: %.2f | Final Price: %.2f\n",
                order.getID(),
                order.getCustoID(), // Assuming typo fixed from getCustoID()
                truncatedTime,
                totalItems,
                itemsDetails.toString(),
                order.getOriginalPrice(),
                order.getTotalDiscount(),
                order.getPrize()
        );

        logEntries.add(logMessage);  // Add the order log message to the log entries list
    }

    /**
     * Logs a generic string message.
     * @param message The message to log.
     */
    public void log(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Log message cannot be null");  // Ensure message is not null
        }
        logEntries.add(message);  // Add the log message to the log entries list
    }

    /**
     * Logs details of a given ReportGenerator.
     * @param reportGenerator The ReportGenerator to log.
     */
    public void log(ReportGenerator reportGenerator) {
        if (reportGenerator == null) {
            throw new IllegalArgumentException("ReportGenerator cannot be null");  // Ensure reportGenerator is not null
        }
        logEntries.add("Report Generated: Total Income = " + reportGenerator.getTotalIncome());  // Log the total income
    }

    /**
     * Logs the state of a given CustomerList.
     * @param customerList The CustomerList to log.
     */
    public void log(CustomerList customerList) {
        if (customerList == null) {
            throw new IllegalArgumentException("CustomerList cannot be null");  // Ensure customerList is not null
        }
        logEntries.add("Customer List Updated: " + customerList.toString());  // Log the updated customer list
    }

    /**
     * Logs the state of a given Menu.
     * @param menu The Menu to log.
     */
    public void log(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null");  // Ensure menu is not null
        }
        logEntries.add("Menu Updated: " + menu.toString());  // Log the updated menu
    }

    /**
     * Saves all log entries to a file and clears the logEntries list to avoid duplication.
     */
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, false))) {
            // Write each log entry to the file
            for (String entry : logEntries) {
                writer.write(entry);
                writer.newLine();  // Add a new line after each entry
            }
            logEntries.clear();  // Clear the log entries list to prevent duplication
        } catch (IOException e) {
            System.err.println("Error saving logs to file: " + e.getMessage());  // Handle file write errors
        }
    }

    /**
     * Returns a copy of the log entries list for testing purposes.
     * @return A copy of the log entries list.
     */
    public List<String> getLogEntries() {
        return new ArrayList<>(logEntries);  // Return a copy of the log entries list to ensure safety
    }
}
