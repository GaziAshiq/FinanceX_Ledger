package com.financex.financex_ledger;

import com.financex.financex_ledger.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserLogin {

    public static int authenticate(String email, String password) {
        String query = "SELECT user_id, password FROM Users WHERE email = ?";

        try (Connection connection = DatabaseConnection.connect(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (PasswordHasher.checkPassword(password, hashedPassword)) {
                    return resultSet.getInt("user_id");  // Return user_id on successful authentication
                }
            }
        } catch (SQLException e) {
            System.out.println("Error during authentication: " + e.getMessage());
        }
        return -1;
    }
}