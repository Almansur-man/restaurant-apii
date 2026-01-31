package kz.aitu.utils;

import kz.aitu.exception.DatabaseOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbCleaner {

    public static void cleanAll() {
        String sql = "TRUNCATE TABLE order_items, orders, menu_items RESTART IDENTITY CASCADE";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to clean database", e);
        }
    }
}
