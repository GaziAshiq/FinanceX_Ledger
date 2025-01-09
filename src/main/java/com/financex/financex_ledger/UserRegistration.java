package com.financex.financex_ledger;

import com.financex.financex_ledger.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserRegistration {
    public static boolean registerUser(String name, String email, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        String query = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";
        int userId = -1;

        try (Connection connection = DatabaseConnection.connect()) {
            assert connection != null;

            // Insert user into the Users table
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, hashedPassword);
                statement.executeUpdate();

                // Retrieve the user ID of the newly registered user
                ResultSet keys = statement.getGeneratedKeys();
                if (keys.next()) {
                    userId = keys.getInt(1);
                }
            }

            // Create a personalized transaction table for the user
            if (userId > 0) {
                DatabaseConnection.createTransactionTableForUser(connection, userId);
            } else {
                System.out.println("Error: User ID could not be retrieved after registration.");
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public static boolean transactionTableForUser(int user_id) {
        String query = "CREATE TABLE IF NOT EXISTS user_" + user_id + "_transactions (\n" +
                "    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    description TEXT NOT NULL,\n" +
                "    debit REAL NOT NULL,\n" +
                "    credit REAL NOT NULL,\n" +
                "    balance REAL NOT NULL,\n" +
                "    savings REAL NOT NULL,\n" +
                "    savings_percentages REAL NOT NULL\n" +
                ");";
        try (Connection connection = DatabaseConnection.connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Transaction table creation failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
