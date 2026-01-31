package kz.aitu.model;

import kz.aitu.exception.InvalidInputException;

public abstract class MenuItem extends BaseEntity
        implements Validatable, PricedItem {

    protected double price;

    public MenuItem(int id, String name, double price) {
        super(id, name);
        this.price = price;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name must not be empty");
        }
        if (price <= 0) {
            throw new InvalidInputException("Price must be greater than 0");
        }
    }
}
