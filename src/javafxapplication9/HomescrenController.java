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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class HomescrenController {

    @FXML
    private Button loginButton;
    
    @FXML
    private ImageView profileImage;

    @FXML
    private TextField searchField;

    @FXML
    private Button searchButton;

    @FXML
    private ListView<String> teacherListView;

    @FXML
    private Label seeMoreLabel;

    private static final String DB_URL = "jdbc:sqlite:users.db";

    @FXML
    private void handleLogin() {
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadUserProfile(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT profile_image FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String imagePath = rs.getString("profile_image");
                profileImage.setImage(new Image(imagePath));
                loginButton.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        teacherListView.getItems().clear();
        String searchText = searchField.getText();

        if (searchText.isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement("SELECT name, subject FROM teachers WHERE name LIKE ?")) {
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String teacherInfo = rs.getString("name") + " - " + rs.getString("subject");
                teacherListView.getItems().add(teacherInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleSeeMore() {
        seeMoreLabel.setStyle("-fx-text-fill: blue;");
        // Load full teacher profiles (to be implemented later)
    }
}