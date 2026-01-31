package kz.aitu.repository;

import kz.aitu.exception.DatabaseOperationException;
import kz.aitu.model.DrinkItem;
import kz.aitu.model.FoodItem;
import kz.aitu.model.MenuItem;
import kz.aitu.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {

    public MenuItem create(MenuItem item) {
        String sql = "INSERT INTO menu_items(name, item_type, price, calories, volume_ml) " +
                "VALUES(?,?,?,?,?) RETURNING id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setString(2, item.getEntityType());
            ps.setDouble(3, item.getPrice());

            if (item instanceof FoodItem fi) {
                ps.setInt(4, fi.getCalories());
                ps.setInt(5, 0);
            } else if (item instanceof DrinkItem di) {
                ps.setInt(4, 0);
                ps.setInt(5, di.getVolumeMl());
            } else {
                ps.setInt(4, 0);
                ps.setInt(5, 0);
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");

                if (item instanceof FoodItem fi) {
                    return new FoodItem(id, fi.getName(), fi.getPrice(), fi.getCalories());
                }
                if (item instanceof DrinkItem di) {
                    return new DrinkItem(id, di.getName(), di.getPrice(), di.getVolumeMl());
                }
            }

            return item;

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while creating menu item", e);
        }
    }

    public List<MenuItem> getAll() {
        String sql = "SELECT id, name, item_type, price, calories, volume_ml " +
                "FROM menu_items ORDER BY id";

        List<MenuItem> items = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);

             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(mapRow(rs));
            }
            return items;

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while getting all menu items", e);
        }
    }

    public MenuItem getById(int id) {
        String sql = "SELECT id, name, item_type, price, calories, volume_ml " +
                "FROM menu_items WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
            return null;

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while getting menu item by id", e);
        }
    }

    public void update(int id, MenuItem item) {
        String sql = "UPDATE menu_items SET name=?, price=?, calories=?, volume_ml=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, item.getName());
            ps.setDouble(2, item.getPrice());

            if (item instanceof FoodItem fi) {
                ps.setInt(3, fi.getCalories());
                ps.setInt(4, 0);
            } else if (item instanceof DrinkItem di) {
                ps.setInt(3, 0);
                ps.setInt(4, di.getVolumeMl());
            } else {
                ps.setInt(3, 0);
                ps.setInt(4, 0);
            }

            ps.setInt(5, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while updating menu item", e);
        }
    }

    public void delete(int id) {
        String deleteOrderItems = "DELETE FROM order_items WHERE menu_item_id=?";
        String deleteMenuItem = "DELETE FROM menu_items WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(deleteOrderItems);
                 PreparedStatement ps2 = conn.prepareStatement(deleteMenuItem)) {

                ps1.setInt(1, id);
                ps1.executeUpdate();

                ps2.setInt(1, id);
                ps2.executeUpdate();

                conn.commit();

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("DB error while deleting menu item", e);
        }
    }


    private MenuItem mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String type = rs.getString("item_type");
        double price = rs.getDouble("price");
        int calories = rs.getInt("calories");
        int volumeMl = rs.getInt("volume_ml");

        if ("FOOD".equals(type)) return new FoodItem(id, name, price, calories);
        return new DrinkItem(id, name, price, volumeMl);
    }
}
