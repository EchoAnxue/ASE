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
    private List<String> logEntries;
    private static final String LOG_FILE = "log.txt";
    private static volatile Logger instance; // Thread-safe singleton

    // Private constructor to prevent external instantiation
    private Logger() {
        this.logEntries = new ArrayList<>();
    }

    /**
     * Gets the unique instance of Logger using double-checked locking.
     * @return Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    /**
     * Logs details of a given Order
     */
    public void logOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        order.getOriginalPrice();
        order.getTotalDiscount();

        StringBuilder itemsDetails = new StringBuilder();
        int totalItems = 0;

        for (Map.Entry<MenuItem, Integer> entry : order.getOrder().entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            totalItems += quantity;

            itemsDetails.append(String.format(
                    "\n  - %s [%s] x%d | %.2f each | Subtotal: %.2f",
                    item.getName(),
                    item.getCategory(),
                    quantity,
                    item.getCost(),
                    item.getCost() * quantity
            ));
        }

        String rawTime = order.getTime();
        String truncatedTime = rawTime.contains(".") ?
                rawTime.split("\\.")[0] : rawTime;

        String logMessage = String.format(
                "Order: ID=%d, CustomerID=%d, Time=%s\n" +
                        "Items (%d):%s\n" +
                        "Original Price: %.2f | Total Discount: %.2f | Final Price: %.2f\n",
                order.getID(),
                order.getCustoID(), // assuming typo fixed from getCustoID()
                truncatedTime,
                totalItems,
                itemsDetails.toString(),
                order.getOriginalPrice(),
                order.getTotalDiscount(),
                order.getPrize()
        );

        logEntries.add(logMessage);
    }

    public void log(String message) {
        if (message == null) {
            throw new IllegalArgumentException("Log message cannot be null");
        }
        logEntries.add(message);
    }

    public void log(ReportGenerator reportGenerator) {
        if (reportGenerator == null) {
            throw new IllegalArgumentException("ReportGenerator cannot be null");
        }
        logEntries.add("Report Generated: Total Income = " + reportGenerator.getTotalIncome());
    }

    public void log(CustomerList customerList) {
        if (customerList == null) {
            throw new IllegalArgumentException("CustomerList cannot be null");
        }
        logEntries.add("Customer List Updated: " + customerList.toString());
    }

    public void log(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null");
        }
        logEntries.add("Menu Updated: " + menu.toString());
    }

    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, false))) {
            for (String entry : logEntries) {
                writer.write(entry);
                writer.newLine();
            }
            logEntries.clear();
        } catch (IOException e) {
            System.err.println("Error saving logs to file: " + e.getMessage());
        }
    }

    public List<String> getLogEntries() {
        return new ArrayList<>(logEntries); // return copy for safety
    }
}
