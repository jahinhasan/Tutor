package javafxapplication9;

import java.io.IOException;
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

    /** ✅ Handle Login Button Click */
    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter email and password.");
            return;
        }

        // Simulate authentication (replace with actual database check)
        if (email.equals("admin@example.com") && password.equals("1234")) {
            showAlert("Login Successful", "Welcome, " + email);
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    /** ✅ Handle Signup Click */
    @FXML
    private void handleSignupClick(MouseEvent event) {
        System.out.println("Signup clicked!"); // Debugging

        try {
            // Load Register.fxml
            Parent registerRoot = FXMLLoader.load(getClass().getResource("register.fxml"));

            // Get current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // Set the new scene
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
