package javafxapplication9;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentDashboardController {

    @FXML
    private TableView<Teacher> teacherTable;
    @FXML
    private TableColumn<Teacher, Integer> addressColumn1;  // for ID
    @FXML
    private TableColumn<Teacher, String> nameColumn;
    @FXML
    private TableColumn<Teacher, String> addressColumn;
    @FXML
    private TableColumn<Teacher, String> subjectColumn;
    @FXML
    private TableColumn<Teacher, String> mobileColumn;

    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

 public void initialize() {
    System.out.println("Initializing student dashboard...");
    addressColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjects"));
    mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));

    loadTeachersFromDatabase();
}

     @FXML
    private void handleProfileButtonAction(ActionEvent event) throws IOException {
        Parent profileRoot = FXMLLoader.load(getClass().getResource("StudentProfile.fxml"));
        Scene profileScene = new Scene(profileRoot);

        // Get current stage and set new scene
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(profileScene);
        window.show();
    }

   private void loadTeachersFromDatabase() {
    System.out.println("Loading teachers from database...");
    String query = "SELECT id, name, address, subjects, mobile FROM teacher_information";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "");
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            System.out.println("Found teacher: " + rs.getString("name"));
            Teacher teacher = new Teacher(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("subjects"),
                    rs.getString("mobile")
            );
            teacherList.add(teacher);
        }

        teacherTable.setItems(teacherList);
        System.out.println("Total teachers loaded: " + teacherList.size());

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
