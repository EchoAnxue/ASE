/**
 * This class is used to manage and log various operations within the system, such as changes in the report generator data,
 * customer list, and menu. Logs are stored in a list and can be saved to a file.
 * @author <Yang Yang> <yy2039@hw.ac.uk>
 * @version 0.01
 * @since 2025-02-12
 * 
 * ****Attributes****:
 * @logEntries: a List to store log entries as strings
 * @LOG_FILE: a constant string representing the file path where logs will be saved
 * 
 * ****Methods****:
 * @Logger(): Constructor to initialize the logEntries list.
 * @log(ReportGenerator reportGenerator): This method logs the total income from the report generator.
 * @log(CustomerList customerList): This method logs the state of the customer list.
 * @log(Menu menu): This method logs the state of the menu.
 * @saveToFile(): This method saves all log entries to a file and clears the logEntries list to avoid duplication.
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Logger {
    private static List<String> logEntries;
    private static final String LOG_FILE = "log.txt";
    private static Logger instance; // Singleton instance

    // Private constructor to prevent external instantiation
    private Logger() {
        this.logEntries = new ArrayList<>();
    }

    /**
     * Gets the unique instance of Logger
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static void logOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        // get total price and discounts
        order.getOriginalPrice();
        order.getTotalDiscount();

        StringBuilder itemsDetails = new StringBuilder();
        int totalItems = 0;

        // iterate over order items and build detailed descriptions
        for (Map.Entry<MenuItem, Integer> entry : order.getOrder().entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            totalItems += quantity;

            itemsDetails.append(String.format(
                    "\n  - %s [%s] x%d | £%.2f each | Subtotal: £%.2f",
                    item.getName(),
                    item.getCategory(),
                    quantity,
                    item.getCost(),
                    item.getCost() * quantity
            ));
        }

        // format the full log
        String logMessage = String.format(
                "Order: ID=%d, CustomerID=%d, Time=%s\n" +
                        "Items (%d):%s\n" +
                        "Original Price: £%.2f | Total Discount: £%.2f | Final Price: £%.2f\n",
                order.getID(),
                order.getCustoID(),
                order.getTime(),
                totalItems,
                itemsDetails.toString(),
                order.getOriginalPrice(),
                order.getTotalDiscount(),
                order.getPrize()
        );

        logEntries.add(logMessage);
    }


    public static void log(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Log message cannot be null");
        }
        logEntries.add(message);
    }

    // Log ReportGenerator data
    public static void log(ReportGenerator reportGenerator) {
        if (reportGenerator == null) {
            throw new IllegalArgumentException("ReportGenerator cannot be null");
        }
        logEntries.add("Report Generated: Total Income = " + reportGenerator.getTotalIncome());
    }

    // Log CustomerList data
    public static void log(CustomerList customerList) {
        if (customerList == null) {
            throw new IllegalArgumentException("CustomerList cannot be null");
        }
        logEntries.add("Customer List Updated: " + customerList.toString());
    }

    // Log Menu data
    public static void log(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        logEntries.add("Menu Updated: " + menu.toString());
    }

    // Save logs to file
    public static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            for (String entry : logEntries) {
                writer.write(entry);
                writer.newLine();
            }
            logEntries.clear();
        } catch (IOException e) {
            System.err.println("Error saving logs to file: " + e.getMessage());
        }
    }

    // Getting log entries (for JUnit test)
    public List<String> getLogEntries() {
        return new ArrayList<>(logEntries); // 返回副本避免外部修改
    }

}
