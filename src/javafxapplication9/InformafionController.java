package javafxapplication9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.image.Image;

public class InformafionController {

    @FXML private RadioButton teacherRadio;
    @FXML private RadioButton studentRadio;
    @FXML private TextField nameField;
    @FXML private TextField mobileField;
    @FXML private TextField educationField;
    @FXML private TextArea addressArea;
    @FXML private TextArea bioArea;
    @FXML private ImageView profileImageView;
    @FXML private Button uploadButton;
    @FXML private Button saveButton;
    @FXML private VBox subjectBox;

    private File selectedImageFile;

    @FXML
    public void initialize() {
        ToggleGroup roleGroup = new ToggleGroup();
        teacherRadio.setToggleGroup(roleGroup);
        studentRadio.setToggleGroup(roleGroup);
    }




@FXML
private void handleUploadImage(ActionEvent event) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Profile Picture");
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
    );
    
    selectedImageFile = fileChooser.showOpenDialog(null);

    if (selectedImageFile != null) {
        // Debugging: Print the selected image file path
        System.out.println("Selected image file: " + selectedImageFile.getAbsolutePath());

        try {
            // Create a FileInputStream to load the image
            FileInputStream inputStream = new FileInputStream(selectedImageFile);
            Image image = new Image(inputStream);

            // Check if the image is valid
            if (image.isError()) {
                System.out.println("Error loading image: " + image.getException());
            } else {
                // Set the Image to the ImageView
                profileImageView.setImage(image);
                profileImageView.setOpacity(1.0);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error: Image file not found.");
        }
    }
}





    @FXML
    private void handleSave(ActionEvent event) {
        String role = teacherRadio.isSelected() ? "Teacher" : "Student";
        String name = nameField.getText();
        String mobile = mobileField.getText();
        String address = addressArea.getText();
        String education = educationField.getText();
        String bio = bioArea.getText();

        ArrayList<String> selectedSubjects = new ArrayList<>();
        for (Node node : subjectBox.getChildren()) {
            if (node instanceof CheckBox && ((CheckBox) node).isSelected()) {
                selectedSubjects.add(((CheckBox) node).getText());
            }
        }

        // Upload the image (File object) here
        File selectedImageFile = this.selectedImageFile;

        // Avoid creating tables every time
        insertUserData(role, name, mobile, address, selectedSubjects, education, bio, selectedImageFile);

        // Navigate to the login screen
        try {
            Parent loginRoot = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertUserData(String role, String name, String mobile, String address,
                                ArrayList<String> selectedSubjects, String education,
                                String bio, File selectedImageFile) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
            // Create the table if it doesn't exist
            String createTableSQL = "CREATE TABLE IF NOT EXISTS " + role + " ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT, "
                    + "mobile TEXT, "
                    + "address TEXT, "
                    + "subjects TEXT, "
                    + "education TEXT, "
                    + "bio TEXT, "
                    + "image BLOB)";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }

            // Convert image to byte array
            byte[] imageBytes = null;
            if (selectedImageFile != null) {
                FileInputStream fis = new FileInputStream(selectedImageFile);
                imageBytes = fis.readAllBytes();
            }

            // Insert user data into the appropriate table
            String insertSQL = "INSERT INTO " + role + " (name, mobile, address, subjects, education, bio, image) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setString(1, name);
                pstmt.setString(2, mobile);
                pstmt.setString(3, address);
                pstmt.setString(4, String.join(",", selectedSubjects));
                pstmt.setString(5, education);
                pstmt.setString(6, bio);
                pstmt.setBytes(7, imageBytes); // Save the image as a byte array
                pstmt.executeUpdate();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            // Optionally, show a user-friendly message about the error
        }
    }

    // Method to display the image from the database path
    private void displayImageFromDatabase(String role, int userId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
            String selectSQL = "SELECT image FROM " + role + " WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    byte[] imageBytes = rs.getBytes("image");
                    if (imageBytes != null) {
                        InputStream is = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(is);
                        profileImageView.setImage(image);
                        profileImageView.setOpacity(1.0); // Ensure the image is visible
                    } else {
                        System.out.println("No image found in database.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Example function to load user data from the database, including image path
    private void loadUserData(String role, int userId) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:users.db")) {
            String selectSQL = "SELECT * FROM " + role + " WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
                pstmt.setInt(1, userId);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String name = rs.getString("name");
                    String mobile = rs.getString("mobile");
                    String address = rs.getString("address");
                    String subjects = rs.getString("subjects");
                    String education = rs.getString("education");
                    String bio = rs.getString("bio");
                    byte[] imageBytes = rs.getBytes("image");

                    // Set fields with data from the database
                    nameField.setText(name);
                    mobileField.setText(mobile);
                    addressArea.setText(address);
                    educationField.setText(education);
                    bioArea.setText(bio);

                    // If image exists, load it
                    if (imageBytes != null) {
                        InputStream is = new ByteArrayInputStream(imageBytes);
                        Image image = new Image(is);
                        profileImageView.setImage(image);
                        profileImageView.setOpacity(1.0); // Ensure the image is visible
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
