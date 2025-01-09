package com.financex.financex_ledger;

import com.financex.financex_ledger.utils.Validation;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginFormController {
    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void loginUser() throws IOException {
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

        int userId = UserLogin.authenticate(email, password);
        String username = DatabaseConnection.getUserName(userId);

        if (userId > 0 && username != null) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setUserDetails(userId, username);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.getScene().setRoot(root);
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
