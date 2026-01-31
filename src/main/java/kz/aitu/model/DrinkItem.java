package kz.aitu.model;

public class DrinkItem extends MenuItem {

    private int volumeMl;

    public DrinkItem(int id, String name, double price, int volumeMl) {
        super(id, name, price);
        this.volumeMl = volumeMl;
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    @Override
    public String getEntityType() {
        return "DRINK";
    }

    @Override
    public String describe() {
        return "DrinkItem{id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", volumeMl=" + volumeMl +
                "}";
    }
}
