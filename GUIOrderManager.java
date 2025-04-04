import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class GUIOrderManager {
    private static GUIOrderNode head;
    private static GUIOrderNode tail;
    private static Map<Integer, GUIOrderNode> orderMap;

    public GUIOrderManager() {
        this.head = null;
        this.tail = null;
        this.orderMap = new HashMap<>();
    }

    public static void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        int id = order.getID();
        if (orderMap.containsKey(id)) {
            throw new IllegalStateException("Order ID already exists: " + id);
        }

        GUIOrderNode newNode = new GUIOrderNode(order);
        if (head == null) {
            head = tail = newNode;
        } else {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
        orderMap.put(id, newNode);
    }

    public static void finishOrder(int orderId) {
        if (!orderMap.containsKey(orderId)) {
            throw new NoSuchElementException("Order ID not found: " + orderId);
        }

        GUIOrderNode node = orderMap.get(orderId);
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
        orderMap.remove(orderId);
    }

    public static Order getOrder() {
        return (head != null) ? head.getOrder() : null;
    }
}