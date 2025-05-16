package javafxapplication9;

import java.io.IOException;
import java.sql.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML private ToggleGroup userTypeToggleGroup;
    @FXML private RadioButton teacherRadio;
    @FXML private RadioButton studentRadio;

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label signupLabel;

    @FXML
    public void initialize() {
       if (teacherRadio != null) {
    teacherRadio.setSelected(true);
}
 // Default selection
    }

    /** ✅ Handle Login Button Click */
    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter both email and password.");
            return;
        }

        // Determine user type (teacher/student)
        RadioButton selectedRadio = (RadioButton) userTypeToggleGroup.getSelectedToggle();
        String userType = selectedRadio.getText(); // "Teacher" or "Student"

        // Determine table and redirect path based on user type
        String table = userType.equals("Teacher") ? "teacher_accounts" : "student_accounts";
        String dashboardFxml = userType.equals("Teacher") ? "TeacherDashboard.fxml" : "StudentDashboard.fxml";

        if (authenticateUser(email, password, table)) {
            showAlert("Login Successful", "Welcome, " + email);
            loadDashboard(dashboardFxml);
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
    }

    /** 🔐 Authenticate against the selected table using MySQL */
    private boolean authenticateUser(String email, String password, String table) {
        String query = "SELECT * FROM " + table + " WHERE gmail = ? AND password = ?";

        try (Connection conn = DBConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if user found

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not connect to the database.");
            return false;
        }
    }

    /** 🚀 Load Dashboard for Teacher or Student */
    private void loadDashboard(String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the dashboard.");
        }
    }
@FXML
private void handleAdminLogin() {
    String email = emailField.getText().trim();
    String password = passwordField.getText().trim();

    // Hardcoded admin credentials
    if (email.equals("admin@tutor.com") && password.equals("admin123")) {
        showAlert("Login Successful", "Welcome Admin");
        loadDashboard("AdminDashboard.fxml");
    } else {
        showAlert("Login Failed", "Invalid admin credentials.");
    }
}

    /** 🧭 Redirect to Signup Page */
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

    /** ⚠️ Show Alert Dialog */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
