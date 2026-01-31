package kz.aitu.model;

public abstract class BaseEntity {

    protected int id;
    protected String name;
    public BaseEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public abstract String getEntityType();
    public abstract String describe();
    public String shortInfo() {
        return id + " - " + name;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
