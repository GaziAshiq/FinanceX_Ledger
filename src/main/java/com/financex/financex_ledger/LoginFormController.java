package com.financex.financex_ledger;

import com.financex.financex_ledger.utils.Validation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginFormController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void loginUser() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        if (!Validation.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Validation Error", "Enter a valid email address.");
            return;
        }

        boolean success = UserLogin.authenticate(email, password);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "You have successfully logged in.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect email or password.");
        }
    }
    @FXML
    private void signupUser() throws IOException {
        Main.setRoot("RegistrationForm");
    }


    private void navigateTo(String s, String ledgerSystem) {
        
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
