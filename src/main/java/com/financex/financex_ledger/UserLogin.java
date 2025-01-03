package com.financex.financex_ledger;

import com.financex.financex_ledger.utils.PasswordHasher;

public class UserLogin {

    public static boolean authenticate(String email, String password) {
        // Retrieve hashed password from the database
        String hashedPassword = DatabaseConnection.getPasswordByEmail(email);

        if (hashedPassword == null) {
            System.out.println("User not found.");
            return false;
        }

        // Check the plain-text password against the hashed password
        if (PasswordHasher.checkPassword(password, hashedPassword)) {
            System.out.println("Login successful.");
            return true;
        } else {
            System.out.println("Incorrect password.");
            return false;
        }
    }
}
