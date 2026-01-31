package kz.aitu.repository;

import kz.aitu.exception.DatabaseOperationException;
import kz.aitu.model.Order;
import kz.aitu.model.OrderItem;
import kz.aitu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public Order create(Order order) {
        String sql = "INSERT INTO orders(customer_name, status) VALUES(?, ?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, order.getCustomerName());
            ps.setString(2, order.getStatus());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int orderId = rs.getInt("id");

                for (OrderItem item : order.getItems()) {
                    addOrderItem(orderId, item.getMenuItemId(), item.getQuantity());
                }
                Order created = new Order(orderId, order.getCustomerName(), order.getStatus());
                for (OrderItem item : order.getItems()) {
                    created.addItem(item);
                }
                return created;
            }

            return order;

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while creating order", e);
        }
    }

    public List<Order> getAll() {
        String sql = "SELECT id, customer_name, status FROM orders ORDER BY id";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("status")
                ));
            }
            return orders;

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while getting all orders", e);
        }
    }

    public Order getById(int id) {
        String sql = "SELECT id, customer_name, status FROM orders WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            Order order = new Order(
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("status")
            );

            String sqlItems = "SELECT menu_item_id, quantity FROM order_items WHERE order_id=?";
            try (PreparedStatement ps2 = conn.prepareStatement(sqlItems)) {
                ps2.setInt(1, id);
                ResultSet rs2 = ps2.executeQuery();

                while (rs2.next()) {
                    order.addItem(new OrderItem(
                            rs2.getInt("menu_item_id"),
                            rs2.getInt("quantity")
                    ));
                }
            }

            return order;

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while getting order by id", e);
        }
    }

    public void updateStatus(int id, String status) {
        String sql = "UPDATE orders SET status=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while updating order status", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM orders WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while deleting order", e);
        }
    }

    private void addOrderItem(int orderId, int menuItemId, int quantity) throws SQLException {
        String sql = "INSERT INTO order_items(order_id, menu_item_id, quantity) VALUES(?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, menuItemId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }
}
