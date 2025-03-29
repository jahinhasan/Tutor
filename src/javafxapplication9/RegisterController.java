package javafxapplication9;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private TextField emailField1; // Gmail field

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField1; // Confirm Password Field

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    // SQLite Database File Path
    private static final String DB_PATH = "users.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    // Constructor: Ensures database and table exist
    public RegisterController() {
        createDatabaseAndTable();
    }

    // ✅ Create Database and Users Table if not exists
    private void createDatabaseAndTable() {
        File dbFile = new File(DB_PATH);
        boolean isNewDatabase = !dbFile.exists();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT NOT NULL, " +
                    "email TEXT NOT NULL UNIQUE, " +
                    "password TEXT NOT NULL" +
                    ");";

            stmt.executeUpdate(sql);

            if (isNewDatabase) {
                System.out.println("✅ New database created: " + DB_PATH);
            } else {
                System.out.println("✅ Database connected successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Handle Register Button Click
    @FXML
    private void handleRegister(ActionEvent event) {
        String username = emailField.getText();
        String email = emailField1.getText();
        String password = passwordField.getText();
        String confirmPassword = passwordField1.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Registration Failed", "All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Registration Failed", "Passwords do not match!");
            return;
        }

        // Save user to database
        if (saveUser(username, email, password)) {
            showAlert("Registration Successful", "Account created successfully!");
            goToLogin(event);
        } else {
            showAlert("Registration Failed", "An error occurred while saving your details.");
        }
    }

    // ✅ Handle Back Button Click
    @FXML
    private void handleBack(ActionEvent event) {
        goToLogin(event);
    }

    // ✅ Save User to SQLite Database
    private boolean saveUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Go to Login Page
    private void goToLogin(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Show Alert Dialogs
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
