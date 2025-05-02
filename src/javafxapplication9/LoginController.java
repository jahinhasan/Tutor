package javafxapplication9;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;  // Email input field

    @FXML
    private PasswordField passwordField;  // Password input field

    @FXML
    private Button loginButton;  // Login button

    @FXML
    private Label signupLabel;  // Signup label to redirect to signup page

    private static final String DB_URL = "jdbc:sqlite:home_tutor.db";  // SQLite Database file (adjust to your actual DB)

    /** ✅ Handle Login Button Click */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();  // Get the email from the field
        String password = passwordField.getText();  // Get the password from the field

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter email and password.");
            return;
        }

        if (authenticateUser(email, password)) {
            showAlert("Login Successful", "Welcome, " + email);
            loadHomeScreen();  // Redirect to home screen after successful login
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    /** 🔗 Authenticate User from Database */
    private boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM teacher_accounts WHERE gmail = ? AND password = ?";  // Updated query for teacher accounts

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);  // Set the Gmail
            stmt.setString(2, password);  // Set the password

            ResultSet rs = stmt.executeQuery();
            return rs.next();  // If a match is found, return true

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
            return false;
        }
    }

    /** 🚀 Redirect to Home Screen */
    private void loadHomeScreen() {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("Homescren.fxml"));  // Load home screen scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));  // Change scene to home screen
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the home screen.");
        }
    }

    /** ✅ Handle Signup Click */
    @FXML
    private void handleSignupClick(MouseEvent event) {
        try {
            Parent registerRoot = FXMLLoader.load(getClass().getResource("register.fxml"));  // Load register scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(registerRoot));  // Change scene to register screen
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ✅ Utility: Show Alerts */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
