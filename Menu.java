import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Menu {

    private HashMap<String,MenuItem> shopList;
    private HashMap<String,MenuItem> beverageList = new HashMap<>();
    private  HashMap<String,MenuItem> foodList = new  HashMap<>();
    private  HashMap<String,MenuItem> dessertList = new  HashMap<>();
    private  HashMap<String,MenuItem> orderList = new  HashMap<>();


    public Menu() throws IOException {
        DataLoader dataLoader = new DataLoader();
        this.shopList = dataLoader.loadMenu();
        for (MenuItem item : this.shopList.values()) {
            if (item.getCategory().equals("beverage")) {
                this.beverageList.put(item.getIdentifier(),item);
            }
            if (item.getCategory().equals("food")) {
                this.foodList.put(item.getIdentifier(),item);
            }
            if (item.getCategory().equals("dessert")) {
                this.dessertList.put(item.getIdentifier(),item);
            }

        }

    }
    public DataLoader getDataLoader() {
        return new DataLoader();
    }
//


    private boolean add(MenuItem item) {
        if (this.shopList.containsKey(item.getIdentifier())) {
            throw new MenuChangeException("duplicate item");

        }
        if (item.getCategory().equals("beverage")) {
            this.beverageList.put(item.getIdentifier(),item);
        }
        if (item.getCategory().equals("food")) {
            this.foodList.put(item.getIdentifier(),item);
        }
        if (item.getCategory().equals("dessert")) {
            this.dessertList.put(item.getIdentifier(),item);
        }

        shopList.put(item.getIdentifier(), item);
        return true;
    }

    private boolean remove(MenuItem item) {
        if (this.shopList.containsKey(item.getIdentifier())) {
            this.shopList.remove(item.getIdentifier());
            if (item.getCategory().equals("beverage")) {
                this.beverageList.remove(item.getIdentifier());
            }
            if (item.getCategory().equals("food")) {
                this.foodList.remove(item.getIdentifier());
            }
            if (item.getCategory().equals("dessert")) {
                this.dessertList.remove(item.getIdentifier());
            } else {
                throw new MenuChangeException("item not found");
            }
        }
        return false;
    }

    private void display(HashMap<String,MenuItem> list) {
        for (MenuItem item : list.values()) {
            System.out.println(item.getName() + " - " + item.getCategory() + " - " + item.getCost());
        }

    }

    public HashMap<String, MenuItem> getBeverageList() {
        return beverageList;
    }

    public HashMap<String, MenuItem> getFoodList() {
        return foodList;
    }

    public HashMap<String, MenuItem> getDessertList() {
        return dessertList;
    }

    public HashMap<String, MenuItem> getOrderList() {
        return orderList;
    }

    public static void main(String args[]) throws IOException {
        Menu menu =new Menu();
        menu.display(menu.shopList);



    }
}


//1.Menu class: I delete loadDataLoader.you can use getFoodList to directly get the list
//2. dataloader class:i would like to delete loadproducts(),casue it seems that we don't need to think about the storage
//    quantity?