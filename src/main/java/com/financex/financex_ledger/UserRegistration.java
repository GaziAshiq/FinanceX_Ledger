package com.financex.financex_ledger;

import com.financex.financex_ledger.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserRegistration {
    public static boolean registerUser(String name, String email, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        String query = "INSERT INTO Users (name, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.connect()) {
            assert connection != null;
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, hashedPassword);
                statement.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
