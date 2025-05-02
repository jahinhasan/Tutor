package javafxapplication9;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    // ✅ Teacher Button
    @FXML
    private void handleRegister(ActionEvent event) {
        loadPage(event, "teacher_information.fxml");
    }

    // ✅ Student Button
    @FXML
    private void handleStudent(ActionEvent event) {
        loadPage(event, "student_information.fxml");
    }

    // ✅ Back Button
    @FXML
    private void handleBack(ActionEvent event) {
        loadPage(event, "login.fxml");
    }

    // ✅ Load Any FXML Page
    private void loadPage(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Navigation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
