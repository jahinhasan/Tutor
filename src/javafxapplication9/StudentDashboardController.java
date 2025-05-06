package javafxapplication9;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class StudentDashboardController implements Initializable {

    @FXML
    private VBox teacherListContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTeacherAdvertisements();
    }

    public void loadTeacherAdvertisements() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Connect to SQLite database
            conn = DriverManager.getConnection("jdbc:sqlite:home_tutor.db");

            // Query to fetch teacher details (columns must exist in your table)
            String sql = "SELECT name, contact, subjects, education, bio FROM teacher_information";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            // Clear previous list
            teacherListContainer.getChildren().clear();

            // Loop through result set and create display for each teacher
            while (rs.next()) {
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String subjects = rs.getString("subjects");
                String education = rs.getString("education");
                String bio = rs.getString("bio");

                // Create a UI block for each teacher (you can style this better)
                VBox teacherBox = new VBox();
                teacherBox.setSpacing(5);
                teacherBox.setStyle("-fx-border-color: #ccc; -fx-padding: 10; -fx-background-color: #f9f9f9; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label nameLabel = new Label("Name: " + name);
                Label contactLabel = new Label("Contact: " + contact);
                Label subjectsLabel = new Label("Subjects: " + subjects);
                Label educationLabel = new Label("Education: " + education);
                Text bioText = new Text("Bio: " + bio);

                teacherBox.getChildren().addAll(nameLabel, contactLabel, subjectsLabel, educationLabel, bioText);

                teacherListContainer.getChildren().add(teacherBox);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
