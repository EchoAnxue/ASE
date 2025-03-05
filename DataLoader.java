import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//for DataLoader, it should load a file of menu. this file
// should include all items and all those details in MenuItem,  then new every
// MenuItem entities for Menu class. try to modify those class diagram to finish this job.
public class DataLoader {
    private static HashMap<String,MenuItem> StorageList = new HashMap<String,MenuItem>();

    private static HashMap<Integer,Order> orderListHash = new HashMap<>();
    private static List<List<Order>> OrderList = new ArrayList<>();
    private static ArrayList<Customer> CustomerList = new ArrayList<>();

        public static void loadMenuItemsFromCSV(String filePath) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        // Skip header if present
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()||line.contains("\t")) {
                break; // 跳过空行
            }
            if(!countComma(line,4)) {
                System.out.println("line: " + line + " has wrong format, should have 4 fields");
                throw new CSVReadException("line: " + line + " has wrong format, should have 10 fields");
            }
            String[] fields = line.replace("\"","").split(",");

            String name = fields[0];
            String category = fields[1].toLowerCase();
            float cost = Float.parseFloat(fields[2]);
            String ID = fields[3];
            String description = fields[4];

            MenuItem item = new MenuItem(name, category, cost, ID, description);
            StorageList.put(ID,item);
        }

        reader.close();


    }
//    "C:\\Users\\lenovo\\IdeaProjects\\ASE\\src\\main\\java\\org\\example\\menu2.csv"
        public static HashMap<String,MenuItem> loadMenu()   {

        try {
            loadMenuItemsFromCSV("./menu.csv");
            // Print loaded items
//            for (MenuItem item : this.StorageList.values()) {
//                System.out.println(item.getName() + " - " + item.getCategory() + " - " + item.getCost());
//            }
        } catch (Exception e) {
            throw new CSVReadException("couldn't read file: " + "./menu.csv");
        }
        return StorageList;
    }
    //  loadproducts() and loadOrder()
// i would like to delete loadproducts(),casue it seems that we don't need to think about the storage
//    quantity
    public static void writeOrder(Order order) throws CSVReadException {
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


            System.out.println("order data has been written to file: " + filePath);
        } catch (Exception e) {
            throw new CSVReadException("couldn't write to file: " + filePath);
        }
    }
 public static void loadOrder() throws CSVReadException, IOException {
    try {
        BufferedReader reader = new BufferedReader(new FileReader("./order.csv"));


        String line;

        // Skip header if present
        reader.readLine();

        while ((line = reader.readLine()) != null) {

            if (line.trim().isEmpty()) {
                break; // 跳过空行
            }
            if(!countComma(line,10)) {
                System.out.println("line: " + line + " has wrong format, should have 10 fields");
                throw new CSVReadException("line: " + line + " has wrong format, should have 10 fields");
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

            if (!orderListHash.containsKey(orderID)) {
                Map<MenuItem, Integer> itemsOrdered= new HashMap<>();
                if(!StorageList.containsKey(itemIdentifier)) {
                    throw new CSVReadException("no this item in storage, maybe some mistakes");
                }
                itemsOrdered.put(StorageList.get(itemIdentifier), quantity);
                orderListHash.put(orderID,new Order(orderID,itemsOrdered,custoID,orderTime
                        ,payment,prize,isRegular,totalDiscount,originalPrice));

            }
            else {
                //exist order, add item to order
                if(!StorageList.containsKey(itemIdentifier)) {
                    throw new CSVReadException("no this item in storage, maybe some mistakes");
                }
                orderListHash.get(orderID).addItem(StorageList.get(itemIdentifier),quantity);

            }


        }

        reader.close();


        for(Map.Entry<Integer,Order> entry : orderListHash.entrySet()) {
            int orderID = entry.getKey();
            int custoID = entry.getValue().getCustoID();

            if(OrderList.size()<=custoID){
                //not exist, create a new list customer{}
                ArrayList<Order> temp = new ArrayList<>();
                temp.add(entry.getValue());
                OrderList.add(temp);
            }
            else{
                //exist customer, add order to list
                OrderList.get(custoID).add(entry.getValue());


            }

        }



    }catch (IOException e){
        throw new IOException("couldn't read file: " + "./order.csv");
    }

 }

    public static List<List<Order>> getOrderList() throws IOException {
        //hashmamp<orderID,Order> orderlisthash
            return OrderList;
    }


    public void setOrderList(HashMap<Integer, Order> orderList) {
        orderList = orderList;
    }
//
    public static ArrayList<Customer> loadCustomersList()  {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("./customer.csv"));
            String line;

            // Skip header if present
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    break; // 跳过空行
                }
                if(!countComma(line,1)) {
                    System.out.println("line: " + line + " has wrong format, should have 1 fields");
                    throw new CSVReadException("line: " + line + " has wrong format, should have 1 fields");
                }

                String[] fields = line.split(",");

                int custoID = Integer.parseInt(fields[0]);
                String name = fields[1];


                Customer customer = new Customer(custoID, name);
                CustomerList.add(customer);
            }

            reader.close();

        } catch (Exception e) {
            throw new CSVReadException("couldn't read file: " + "./customer.csv");
        }
        return CustomerList;



    }

    public static ArrayList<Customer> getCustomerList() {
        return CustomerList;
    }

    public static void writeCustomersList(ArrayList<Customer> customerList) {

        String filePath = "./customer.csv";
        for(Customer customer : customerList) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                CustomerEntry customerEntry = new CustomerEntry(customer.getID(), customer.getName());
                writer.write(customerEntry.toString());
                writer.newLine();
            } catch (Exception e) {
                throw new CSVReadException("couldn't write to file: " + filePath);
            }
        }

        }
        public static void writeACustomer(Customer customer){
            String filePath = "./customer.csv";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                CustomerEntry customerEntry = new CustomerEntry(customer.getID(), customer.getName());
                if (CustomerList.get(customer.getID())==null) {
                    writer.write(customerEntry.toString());
                    writer.newLine();
                }
                else {
                    System.out.println("customer already exist");
                }

            } catch (Exception e) {
                e.printStackTrace();
                throw new CSVReadException("couldn't write to file: " + filePath);
            }


        }



    private static boolean countComma(String str, int n) {

            String line = str.trim();
        return line.length() - line.replace(",", "").length()==n;
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

class CustomerEntry {
    private int CustomerID;
    private String name;
        public CustomerEntry(int CustomerID, String name) {
        this.CustomerID = CustomerID;
        this.name = name;
    }
    public String toString() {
        return this.CustomerID + "," + this.name;
    }
}