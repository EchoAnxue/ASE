import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//for DataLoader, it should load a file of menu. this file
// should include all items and all those details in MenuItem,  then new every
// MenuItem entities for Menu class. try to modify those class diagram to finish this job.
public class DataLoader {
    private HashMap<String,MenuItem> StorageList = new HashMap<String,MenuItem>();
    private HashMap<Integer,Order>  orderList = new HashMap<>();

        private void loadMenuItemsFromCSV(String filePath) throws IOException {
        this.StorageList = new HashMap<String,MenuItem>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip header if present
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                break; // 跳过空行
            }

            String[] fields = line.split("\t");

            String name = fields[0];
            String category = fields[1];
            float cost = Float.parseFloat(fields[2]);
            String ID = fields[3];
            String description = fields[4];

            MenuItem item = new MenuItem(name, category, cost, ID, description);
            StorageList.put(name,item);
        }

        reader.close();


    }
//    "C:\\Users\\lenovo\\IdeaProjects\\ASE\\src\\main\\java\\org\\example\\menu2.csv"
        public HashMap<String,MenuItem> loadMenu() throws IOException {

        try {
            loadMenuItemsFromCSV("./menu.csv");
            // Print loaded items
//            for (MenuItem item : this.StorageList.values()) {
//                System.out.println(item.getName() + " - " + item.getCategory() + " - " + item.getCost());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.StorageList;
    }
    // TODO loadproducts() and loadOrder()
// i would like to delete loadproducts(),casue it seems that we don't need to think about the storage
//    quantity
    public void writeOrder(Order order) throws IOException {
            String orderID = String.valueOf(order.getID());
            String custoID = String.valueOf(order.getCustoID());
            boolean payment = order.getPaymentStatus();
            boolean isRegular = order.isRegularCustomer();






        String filePath = "./order.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            for(Map.Entry<MenuItem, Integer> entry : order.getOrder().entrySet()) {
                MenuItem item = entry.getKey();
                int quantity = entry.getValue();
                OrderEntry orderentry = new OrderEntry(orderID, custoID, order.getTime(),
                        item.getIdentifier(), item.getName(), quantity,payment, order.getPrize(),isRegular, order.getTotalDiscount(), order.getOriginalPrice());
                writer.write(orderentry.toString());
                writer.newLine();
            }


            System.out.println("订单数据已成功写入 " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 public void loadOrder() throws IOException {
     this.orderList = new HashMap<Integer,Order>();
     BufferedReader reader = new BufferedReader(new FileReader("./order.csv"));
     String line;

     // Skip header if present
     reader.readLine();

     while ((line = reader.readLine()) != null) {

         if (line.trim().isEmpty()) {
             break; // 跳过空行
         }

         String[] fields = line.split(",");
//         String orderID, String CustomerID, String Time, String itemID,
//                 String itemName, int quantity,boolean payment,float prize,boolean isRegularCustomer,
//         float totalDiscount,float originalPrice)

         int orderID = Integer.parseInt(fields[0]);
         int custoID = Integer.parseInt(fields[1]);
         String orderTime = fields[2];
         String itemIdentifier = fields[3];
         String itemName = fields[4];
         int quantity = Integer.parseInt(fields[5]);
         boolean payment = Boolean.parseBoolean(fields[6]);
         float prize = Float.parseFloat(fields[7]);
         boolean isRegular = Boolean.parseBoolean(fields[8]);
         float totalDiscount = Float.parseFloat(fields[9]);
         float originalPrice = Float.parseFloat(fields[10]);

         if (!this.orderList.containsKey(orderID)) {
             Map<MenuItem, Integer> itemsOrdered= new HashMap<>();
             if(!this.StorageList.containsKey(itemIdentifier)) {
                 throw new CSVReadException("no this item in storage, maybe some mistakes");
             }
             itemsOrdered.put(this.StorageList.get(itemIdentifier), quantity);
             this.orderList.put(orderID,new Order(orderID,itemsOrdered,custoID,orderTime
             ,payment,prize,isRegular,totalDiscount,originalPrice));
             System.out.println("1");
         }
         else {
             if(!this.StorageList.containsKey(itemIdentifier)) {
                 throw new CSVReadException("no this item in storage, maybe some mistakes");
             }
             this.orderList.get(orderID).addItem(this.StorageList.get(itemIdentifier),quantity);
             System.out.println("2");
         }


     }

     reader.close();
 }

    public HashMap<Integer, Order> getOrderList() {
        return orderList;
    }


    public void setOrderList(HashMap<Integer, Order> orderList) {
        this.orderList = orderList;
    }
}
class OrderEntry {
    private String orderID;
    private String CustomerID;
    private String Time;
    private String itemID;
    private String itemName;
    private int quantity;
    private boolean payment;
    private float prize;
    private boolean isRegularCustomer;
    private float totalDiscount;
    private float originalPrice;
    public OrderEntry(String orderID, String CustomerID, String Time, String itemID,
                      String itemName, int quantity,boolean payment,float prize,boolean isRegularCustomer,
                      float totalDiscount,float originalPrice) {
        this.orderID = orderID;
        this.CustomerID = CustomerID;
        this.Time = Time;
        this.itemID = itemID;
        this.itemName = itemName;
        this.quantity = quantity;
        this.payment = payment;
        this.prize = prize;
        this.isRegularCustomer = isRegularCustomer;
        this.totalDiscount = totalDiscount;
        this.originalPrice = originalPrice;





    }
    public String toString() {
        String entry = this.orderID+","+this.CustomerID+","+this.Time+","+this.itemID+","+this.itemName+","+
                this.quantity+","+this.payment+","+this.prize+","+this.isRegularCustomer+","+this.totalDiscount+","+this.originalPrice;
        return entry;
    }

}
