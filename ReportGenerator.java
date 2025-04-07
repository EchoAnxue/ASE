import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ReportGenerator {
    private static Map<MenuItem, Integer> itemOrderCount = new HashMap<>();
    private static float totalIncome = 0;
    private static float totalDiscount = 0;
    private static float finalIncome = 0;

    public static void countInformation(OrderManager orderManager) {
        itemOrderCount.clear();
        totalIncome = 0;
        totalDiscount = 0;

        if (orderManager == null) {
            throw new IllegalArgumentException("OrderManager cannot be null");
        }

        List<List<Order>> finalOrderList = orderManager.getAllOrder();

        for (List<Order> orders : finalOrderList) {
            for (Order order : orders) {
                if (order == null) continue;

                totalIncome += order.getPrize();
                totalDiscount += order.getTotalDiscount();

                Map<MenuItem, Integer> itemsOrdered = order.getOrder();
                for (Map.Entry<MenuItem, Integer> entry : itemsOrdered.entrySet()) {
                    MenuItem item = entry.getKey();
                    int quantity = entry.getValue();

                    if (item == null || quantity < 0) continue;

                    itemOrderCount.put(item, itemOrderCount.getOrDefault(item, 0) + quantity);
                }
            }
        }

        finalIncome = totalIncome - totalDiscount;
    }

    // Generate report.txt file
    public static void printReport() {
        try {
            printReport("report.txt");
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }

    public static void printReport(String filename) throws IOException {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("============== Sales Report ==============\n");
            writer.write("Menu Item Sales:\n");

            for (Map.Entry<MenuItem, Integer> entry : itemOrderCount.entrySet()) {
                writer.write(String.format("%-20s %d\n", entry.getKey().getName(), entry.getValue()));
            }

            writer.write("-------------------------------------------\n");
            writer.write(String.format("Total Income: $%.2f\n", totalIncome));
            writer.write(String.format("Total Discount: $%.2f\n", totalDiscount));
            writer.write(String.format("Final Income: $%.2f\n", finalIncome));
            writer.write("===========================================\n");
        }
    }

    public static Map<MenuItem, Integer> getOrderCount() {
        return itemOrderCount;
    }

    public static float getTotalIncome() {
        return totalIncome;
    }

    public static float getTotalDiscount() {
        return totalDiscount;
    }

    public float getFinalIncome() {
        return finalIncome;
    }
}
