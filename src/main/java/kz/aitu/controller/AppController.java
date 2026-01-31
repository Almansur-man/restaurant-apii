package kz.aitu.controller;

import kz.aitu.model.*;
import kz.aitu.service.MenuItemService;
import kz.aitu.service.OrderService;

public class AppController {

    private final MenuItemService menuService = new MenuItemService();
    private final OrderService orderService = new OrderService();

    public void runDemo() {
        System.out.println("=== DEMO START ===");
        kz.aitu.utils.DbCleaner.cleanAll();
        System.out.println("Database cleaned.");
        System.out.println("Database cleaned.");


        BaseEntity base1 = new FoodItem(0, "Pizza", 3000, 900);
        BaseEntity base2 = new DrinkItem(0, "Tea", 500, 250);

        System.out.println("\nPolymorphism example:");
        System.out.println(base1.describe());
        System.out.println(base2.describe());

        System.out.println("\nCreate menu items:");
        MenuItem createdFood = menuService.create((MenuItem) base1);
        MenuItem createdDrink = menuService.create((MenuItem) base2);

        System.out.println("Created: " + createdFood.shortInfo());
        System.out.println("Created: " + createdDrink.shortInfo());

        System.out.println("\nAll menu items (getPrice() via interface):");
        for (MenuItem item : menuService.getAll()) {
            System.out.println(item.describe() + " | price=" + item.getPrice());
        }

        System.out.println("\nUpdate food item:");
        MenuItem updatedFood = new FoodItem(0, "Pizza BIG", 3500, 1100);
        menuService.update(createdFood.getId(), updatedFood);
        System.out.println("After update: " + menuService.getById(createdFood.getId()).describe());

        System.out.println("\nCreate order (composition):");
        Order order = new Order(0, "Mansur", "NEW");
        order.addItem(new OrderItem(createdFood.getId(), 1));
        order.addItem(new OrderItem(createdDrink.getId(), 2));

        Order createdOrder = orderService.create(order);
        System.out.println("Created order id=" + createdOrder.getId());

        System.out.println("\nGet order by id:");
        Order loaded = orderService.getById(createdOrder.getId());
        System.out.println("Order: id=" + loaded.getId()
                + ", customer=" + loaded.getCustomerName()
                + ", status=" + loaded.getStatus());
        System.out.println("Items count=" + loaded.getItems().size());

        System.out.println("\nTrigger validation error:");
        try {
            menuService.create(new DrinkItem(0, "", -1, 200));
        } catch (Exception ex) {
            System.out.println("Caught: " + ex.getClass().getSimpleName() + " -> " + ex.getMessage());
        }

        System.out.println("\nTrigger duplicate error:");
        try {
            menuService.create(new FoodItem(0, "Pizza BIG", 3500, 1100));
        } catch (Exception ex) {
            System.out.println("Caught: " + ex.getClass().getSimpleName() + " -> " + ex.getMessage());
        }

        System.out.println("\nPay order:");
        orderService.payOrder(createdOrder.getId());
        System.out.println("Order paid.");

        System.out.println("\nTry delete PAID order (should fail):");
        try {
            orderService.delete(createdOrder.getId());
        } catch (Exception ex) {
            System.out.println("Caught: " + ex.getClass().getSimpleName() + " -> " + ex.getMessage());
        }

        System.out.println("\nDelete drink item:");
        menuService.delete(createdDrink.getId());
        System.out.println("Deleted drink item id=" + createdDrink.getId());

        System.out.println("\n=== DEMO END ===");
    }
}
