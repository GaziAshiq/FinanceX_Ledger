package com.financex.financex_ledger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class RegistrationForm extends Application {

    public static void main(String[] args) {
        // Initialize the database
        DatabaseConnection.initializeDatabase();

        // Launch the application
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println(getClass().getResource("/com/financex/financex_ledger/RegistrationForm.fxml")); // Debug path
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/financex/financex_ledger/RegistrationForm.fxml")));
            primaryStage.setTitle("Ledger System - Registration Form");
            primaryStage.setScene(new Scene(root, 400, 200));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Failed to load RegistrationForm.fxml: " + e.getMessage());
//            e.printStackTrace();
        }
    }
}