package com.financex.financex_ledger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/financex/financex_ledger/LoginForm.fxml")));
            primaryStage.setTitle("Ledger System - Login");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
        } catch (Exception e) {
            System.out.println("Failed to load LoginForm.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        launch(args);
    }
}
