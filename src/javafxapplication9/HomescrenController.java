package javafxapplication9;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomescrenController {

    @FXML
    private Button loginButton;

    @FXML
    private Button loginButton1; // This is actually the Sign up button

    /** 🧭 Go to Login Page */
    @FXML
    private void handleLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));  // Adjust filename if needed
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 🧭 Go to Register Page */
    @FXML
    private void handleSignup() {
        try {
            Parent signupRoot = FXMLLoader.load(getClass().getResource("register.fxml"));  // Adjust filename if needed
            Stage stage = (Stage) loginButton1.getScene().getWindow();
            stage.setScene(new Scene(signupRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 🌟 Redirect both buttons to their correct destinations */
    
}
