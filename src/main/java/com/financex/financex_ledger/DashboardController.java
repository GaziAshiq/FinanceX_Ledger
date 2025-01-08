package com.financex.financex_ledger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label balanceLabel;

    @FXML
    private Label savingsLabel;

    @FXML
    private Label loanLabel;

    private String username;

    public void initialize() {
        // simulate user data
        username = "John Doe";
        double balance = 1059.93;
        double savings = 200.00;
        double loan = 500.00;

        welcomeLabel.setText("Welcome, " + username);
        balanceLabel.setText(String.format("%.2f", balance));
        savingsLabel.setText(String.format("%.2f", savings));
        loanLabel.setText(String.format("%.2f", loan));
    }

    @FXML
    private void handleDebit() {
        System.out.println("Navigate to Debit Screen");
    }

    @FXML
    private void handleCredit() {
        System.out.println("Navigate to Credit Screen");
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
}
