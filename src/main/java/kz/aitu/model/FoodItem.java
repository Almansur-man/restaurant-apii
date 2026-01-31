package kz.aitu.model;

public class FoodItem extends MenuItem {

    private int calories;

    public FoodItem(int id, String name, double price, int calories) {
        super(id, name, price);
        this.calories = calories;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String getEntityType() {
        return "FOOD";
    }

    @Override
    public String describe() {
        return "FoodItem{id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", calories=" + calories +
                "}";
    }
}
