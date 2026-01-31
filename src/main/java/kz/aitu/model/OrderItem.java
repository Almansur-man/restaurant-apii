package kz.aitu.model;

public class OrderItem {

    private final int menuItemId;
    private final int quantity;

    public OrderItem(int menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
