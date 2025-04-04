public class GUIOrderNode {
    private int orderId;     // 订单唯一标识
    private Order order;   // 订单数据（可自定义类型）
    private GUIOrderNode prev;     // 前驱节点
    private GUIOrderNode next;     // 后继节点

    public GUIOrderNode(Order order) {
        this.orderId = order.getID();
        this.order = order;
        this.prev = null;
        this.next = null;
    }

    // Getters and Setters
    public int getOrderID() { return orderId; }
    public Order getOrder() { return order; }
    public GUIOrderNode getPrev() { return prev; }
    public void setPrev(GUIOrderNode prev) { this.prev = prev; }
    public GUIOrderNode getNext() { return next; }
    public void setNext(GUIOrderNode next) { this.next = next; }
}