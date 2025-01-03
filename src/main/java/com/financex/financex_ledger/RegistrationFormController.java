package com.financex.financex_ledger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.financex.financex_ledger.utils.Validation;

public class RegistrationFormController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
            return;
        }

        if (!Validation.isValidName(name)) {
            showAlert(Alert.AlertType.ERROR, "Name Validation Error", "Name must be at least 3 characters long.");
            return;
        }

        if (!Validation.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Email Validation Error", "Enter a valid email address.");
            return;
        }

        if (!Validation.isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Password Validation Error", "Password must be at least 8 characters long and include letters and numbers.");
            return;
        }

        if (!Validation.doPasswordsMatch(password, confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Validation Error", "Passwords do not match.");
            return;
        }

        boolean success = UserRegistration.registerUser(name, email, password);
        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "You have successfully registered.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "Could not register. Please try again.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
