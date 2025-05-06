package javafxapplication9;

import com.jfoenix.controls.JFXTextArea;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.VBox;


public class teacher_informationController {

    @FXML private TextField nameField, mobileField, educationField, gmailField, userIdField, passwordField, confirmPasswordField;
    @FXML private JFXTextArea addressArea;
    @FXML private TextArea bioArea;
    @FXML private VBox subjectBox;
    @FXML private ImageView profileImageView;
    @FXML private Button uploadButton, saveButton;

    private File selectedImageFile;
    
    // ✅ Update your DB connection details here
    private static final String DB_URL = "jdbc:mysql://localhost:3306/home_tutor";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(uploadButton.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            Image image = new Image(file.toURI().toString());
            profileImageView.setImage(image);
            profileImageView.setOpacity(1.0);
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String education = educationField.getText();
        String address = addressArea.getText();
        String bio = bioArea.getText();
        String gmail = gmailField.getText();
        String userId = userIdField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        List<String> subjects = getSelectedSubjects();

        if (name.isEmpty() || mobile.isEmpty() || education.isEmpty() || address.isEmpty() || bio.isEmpty()
                || gmail.isEmpty() || userId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || selectedImageFile == null) {
            showAlert("Error", "Please fill in all fields and upload an image.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        createTablesIfNotExists();

        try (Connection conn = DBConnector.getConnection()) {
            conn.setAutoCommit(false);

            String insertAccountSQL = "INSERT INTO teacher_accounts(userId, gmail, password) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertAccountSQL)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, gmail);
                pstmt.setString(3, password);
                pstmt.executeUpdate();
            }

            String insertInfoSQL = "INSERT INTO teacher_information(userId, name, mobile, address, education, bio, subjects, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertInfoSQL)) {
                pstmt.setString(1, userId);
                pstmt.setString(2, name);
                pstmt.setString(3, mobile);
                pstmt.setString(4, address);
                pstmt.setString(5, education);
                pstmt.setString(6, bio);
                pstmt.setString(7, String.join(",", subjects));
                pstmt.setBytes(8, imageToBytes(selectedImageFile));
                pstmt.executeUpdate();
            }

            conn.commit();

            showAlert("Success", "Teacher registered successfully!");
            goToLogin(event);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }

    private void createTablesIfNotExists() {
        try (Connection conn = DBConnector.getConnection();
             Statement stmt = conn.createStatement()) {

            String createAccountTable = "CREATE TABLE IF NOT EXISTS teacher_accounts (" +
                    "userId VARCHAR(255) PRIMARY KEY," +
                    "gmail VARCHAR(255) NOT NULL," +
                    "password VARCHAR(255) NOT NULL" +
                    ") ENGINE=InnoDB;";

            String createInfoTable = "CREATE TABLE IF NOT EXISTS teacher_information (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "userId VARCHAR(255)," +
                    "name VARCHAR(255), mobile VARCHAR(50), address TEXT, education VARCHAR(255), bio TEXT, subjects TEXT, image LONGBLOB," +
                    "FOREIGN KEY(userId) REFERENCES teacher_accounts(userId) ON DELETE CASCADE" +
                    ") ENGINE=InnoDB;";

            stmt.executeUpdate(createAccountTable);
            stmt.executeUpdate(createInfoTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private byte[] imageToBytes(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String format = file.getName().endsWith(".png") ? "png" : "jpg";
        ImageIO.write(bufferedImage, format, baos);
        return baos.toByteArray();
    }

    private List<String> getSelectedSubjects() {
        List<String> subjects = new ArrayList<>();
        subjectBox.getChildren().forEach(node -> {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    subjects.add(checkBox.getText());
                }
            }
        });
        return subjects;
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void goToLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to go to login page.");
        }
    }
}