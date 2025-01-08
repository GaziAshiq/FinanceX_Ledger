//package com.financex.financex_ledger;
//
//import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.util.Objects;
//
//public class WelcomeScreenController {
//
//    @FXML
//    private void goToLogin() {
//        navigateTo("/com/financex/financex_ledger/LoginForm.fxml", "Ledger System - Login");
//    }
//
//    @FXML
//    private void goToRegister() {
//        navigateTo("/com/financex/financex_ledger/RegistrationForm.fxml", "Ledger System - Register");
//    }
//
//    private void navigateTo(String fxmlPath, String title) {
//        try {
////            Stage stage = (Stage) FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath))).getClass().getWindow();
//            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
//            stage.setTitle(title);
//            stage.setScene(new Scene(root));
//        } catch (IOException e) {
//            System.out.println("Failed to load " + fxmlPath + ": " + e.getMessage());
//        }
//    }
//}
