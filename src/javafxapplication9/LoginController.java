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
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label signupLabel;

    private static final String DB_URL = "jdbc:sqlite:users.db"; // SQLite Database

    /** ✅ Handle Login Button Click */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter email and password.");
            return;
        }

        if (authenticateUser(email, password)) {
            showAlert("Login Successful", "Welcome, " + email);
            loadHomeScreen(); // Load home screen after successful login
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    /** 🔗 Authenticate User from Database */
    private boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // If there is a match, return true

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
            return false;
        }
    }

    /** 🚀 Redirect to Home Screen */
    private void loadHomeScreen() {
        try {
            Parent homeRoot = FXMLLoader.load(getClass().getResource("Homescren.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(homeRoot));
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
            Parent registerRoot = FXMLLoader.load(getClass().getResource("register.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(registerRoot));
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
