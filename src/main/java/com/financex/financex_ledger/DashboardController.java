package com.financex.financex_ledger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

import static com.financex.financex_ledger.DatabaseConnection.connect;
import static com.financex.financex_ledger.DatabaseConnection.getUserBalance;

public class DashboardController {
    private String username;
    private int userId;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label savingsLabel;

    @FXML
    private Label loanLabel;

    public void initialize(int userId) {
        // Fetch balance, savings, and loans from the database
        double balance = getUserBalance(userId);
        double savings = DatabaseConnection.getUserSavings(userId);
        double loan = DatabaseConnection.getUserLoan(userId);

        welcomeLabel.setText("Welcome, " + username);
        balanceLabel.setText(String.format("%.2f", balance));
        savingsLabel.setText(String.format("%.2f", savings));
        loanLabel.setText(String.format("%.2f", loan));
    }

    public static double addTransaction(int userId, String description, double debit, double credit) {
        String tableName = "user_" + userId + "_transactions";
        String query = "INSERT INTO " + tableName + " (description, debit, credit, balance) VALUES (?, ?, ?, ?)";
        double currentBalance = getUserBalance(userId);
        double newBalance = currentBalance + credit - debit;

        try (Connection connection = connect(); PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setDouble(2, debit);
            statement.setDouble(3, credit);
            statement.setDouble(4, newBalance);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding transaction: " + e.getMessage());
            return -1; // Indicate failure
        }

        return newBalance;
    }


    @FXML
    private void handleDebit() {
        double debitAmount = promptForAmount("Enter Debit Amount:");
        if (debitAmount <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Amount must be positive.");
            return;
        }

        String description = promptForDescription();
        double newBalance = DatabaseConnection.addTransaction(userId, description, debitAmount, 0);

        if (newBalance >= 0) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Debit added successfully.");
            refreshDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add debit.");
        }
    }


    @FXML
    private void handleCredit() {
        double creditAmount = promptForAmount("Enter Credit Amount:");
        if (creditAmount <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Amount must be positive.");
            return;
        }

        String description = promptForDescription();
        double newBalance = DatabaseConnection.addTransaction(userId, description, 0, creditAmount);

        if (newBalance >= 0) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Credit added successfully.");
            refreshDashboard();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add credit.");
        }
    }



    @FXML
    private void viewHistory() {
        System.out.println("Navigate to History Screen");
    }

    @FXML
    private void manageSavings() {
        System.out.println("Navigate to Savings Management Screen");
    }

    @FXML
    private void manageCreditLoan() {
        System.out.println("Navigate to Credit Loan Management Screen");
    }

    @FXML
    private void predictDepositInterest() {
        System.out.println("Navigate to Deposit Interest Predictor Screen");
    }

    @FXML
    private void logout() {
        System.out.println("Logging out...");
        // Redirect to Login Screen
    }

    private double promptForAmount(String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText(null);
        dialog.setContentText(message);

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                return Double.parseDouble(result.get());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number.");
            }
        }
        return 0.0;
    }

    private String promptForDescription() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input Required");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter description:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse("");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserDetails(int userId, String username) {
        this.userId = userId;
        this.username = username;
        refreshDashboard();
    }

    private void refreshDashboard() {
        // Set welcome message
        welcomeLabel.setText("Welcome, " + username);

        // Fetch account details
        double balance = getUserBalance(userId);
        double savings = DatabaseConnection.getUserSavings(userId);
        double loan = DatabaseConnection.getUserLoan(userId);

        // Update labels
        balanceLabel.setText(String.format("%.2f", balance));
        savingsLabel.setText(String.format("%.2f", savings));
        loanLabel.setText(String.format("%.2f", loan));
    }


}
