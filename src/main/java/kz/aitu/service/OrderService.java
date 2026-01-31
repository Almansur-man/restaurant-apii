package kz.aitu.service;

import kz.aitu.exception.InvalidInputException;
import kz.aitu.exception.ResourceNotFoundException;
import kz.aitu.model.Order;
import kz.aitu.model.OrderItem;
import kz.aitu.repository.MenuItemRepository;
import kz.aitu.repository.OrderRepository;

import java.util.List;

public class OrderService {

    private final OrderRepository orderRepo = new OrderRepository();
    private final MenuItemRepository menuRepo = new MenuItemRepository();

    public Order create(Order order) {

        if (order.getCustomerName() == null || order.getCustomerName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name must not be empty");
        }

        if (order.getItems().isEmpty()) {
            throw new InvalidInputException("Order must contain at least 1 item");
        }

        for (OrderItem oi : order.getItems()) {
            if (menuRepo.getById(oi.getMenuItemId()) == null) {
                throw new InvalidInputException("Menu item id does not exist: " + oi.getMenuItemId());
            }
        }

        if (order.getStatus() == null || order.getStatus().isBlank()) {
            order.setStatus("NEW");
        }

        return orderRepo.create(order);
    }

    public List<Order> getAll() {
        return orderRepo.getAll();
    }

    public Order getById(int id) {
        Order order = orderRepo.getById(id);
        if (order == null) {
            throw new ResourceNotFoundException("Order not found id=" + id);
        }
        return order;
    }

    public void payOrder(int id) {
        Order order = getById(id);

        if ("CANCELLED".equals(order.getStatus())) {
            throw new InvalidInputException("Cannot pay CANCELLED order");
        }

        orderRepo.updateStatus(id, "PAID");
    }

    public void cancelOrder(int id) {
        Order order = getById(id);

        if ("PAID".equals(order.getStatus())) {
            throw new InvalidInputException("Cannot cancel PAID order");
        }

        orderRepo.updateStatus(id, "CANCELLED");
    }

    public void delete(int id) {
        Order order = getById(id);

        if ("PAID".equals(order.getStatus())) {
            throw new InvalidInputException("Cannot delete PAID order");
        }

        orderRepo.delete(id);
    }
}
