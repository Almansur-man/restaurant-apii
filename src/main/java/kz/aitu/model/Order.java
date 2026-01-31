package kz.aitu.model;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int id;
    private String customerName;
    private String status;

    private final List<OrderItem> items = new ArrayList<>();

    public Order(int id, String customerName, String status) {
        this.id = id;
        this.customerName = customerName;
        this.status = status;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
