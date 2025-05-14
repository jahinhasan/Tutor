package javafxapplication9;

import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class StudentProfileController implements Initializable {

    @FXML private Label lblName;
    @FXML private Label lblEmail;
    @FXML private Label lblContact;
    @FXML private TextArea lblAddress;
    @FXML private Label lblSubjects;
    @FXML private Label lblEducation;
    @FXML private TextArea lblBio;
    @FXML private ImageView imageProfile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        String loggedInStudentId = Session.getStudentId(); // Make sure this returns the correct ID
        loadStudentProfile(loggedInStudentId);
    }

    private void loadStudentProfile(String studentId) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "")) {
String query = "SELECT * FROM student_information WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                lblName.setText(rs.getString("name"));
                lblEmail.setText(rs.getString("email"));
                lblContact.setText(rs.getString("contact"));
                lblAddress.setText(rs.getString("address"));
                lblSubjects.setText(rs.getString("selected_subjects"));
                lblEducation.setText(rs.getString("education"));
                lblBio.setText(rs.getString("bio"));

                // Load profile image
                InputStream is = rs.getBinaryStream("image");
                if (is != null) {
                    Image image = new Image(is);
                    imageProfile.setImage(image);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
