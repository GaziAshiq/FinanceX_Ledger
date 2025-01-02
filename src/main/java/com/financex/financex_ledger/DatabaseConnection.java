package com.financex.financex_ledger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:financex_ledger";
//    private static final String URL = "jdbc:sqlite:C:/path/to/financex_ledger.db";


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

