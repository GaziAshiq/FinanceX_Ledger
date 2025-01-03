package com.financex.financex_ledger;

import java.sql.*;

public class DatabaseConnection {
    public static String getPasswordByEmail(String email) {
        String query = "SELECT password FROM Users WHERE email = ?";

        try (Connection conn = connect()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving password: " + e.getMessage());
        }
        return null;
    }


    private static final String URL = "jdbc:sqlite:financex_ledger";

    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    public static void initializeDatabase() {
        String createUsersTableSQL = """
                    CREATE TABLE IF NOT EXISTS Users (
                        user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        name TEXT NOT NULL,
                        email TEXT UNIQUE NOT NULL,
                        password TEXT NOT NULL
                    );
                """;

        try (Connection connection = connect()) {
            assert connection != null;
            try (Statement statement = connection.createStatement()) {
                statement.execute(createUsersTableSQL);
                System.out.println("Database initialized successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

