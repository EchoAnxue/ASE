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

public class Logger {
    private List<String> logEntries;
    private static final String LOG_FILE = "log.txt";

    public Logger() {
        this.logEntries = new ArrayList<>();
    }

    // Log ReportGenerator data
    public void log(ReportGenerator reportGenerator) {
        logEntries.add("Report Generated: Total Income = " + reportGenerator.getTotalIncome());
    }

    // Log CustomerList data
    public void log(CustomerList customerList) {
        logEntries.add("Customer List Updated: " + customerList.toString());
    }

    // Log Menu data
    public void log(Menu menu) {
        logEntries.add("Menu Updated: " + menu.toString());
    }

    // Save logs to file
    public void saveToFile() {
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
}
