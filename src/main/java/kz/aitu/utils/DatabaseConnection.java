package kz.aitu.utils;

import kz.aitu.exception.DatabaseOperationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:postgresql://localhost:1234/restaurant_api";

    private static final String USER = "postgres";
    private static final String PASSWORD = "Almansur09";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new DatabaseOperationException(
                    "Failed to connect to database",
                    e
            );
        }
    }
}
