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

public class TeacherDashboardController {

    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, Integer> idColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> addressColumn;

    @FXML
    private TableColumn<Student, String> subjectColumn;

    @FXML
    private TableColumn<Student, String> mobileColumn;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    public void initialize() {
        System.out.println("Initializing teacher dashboard...");

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subjects"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));

        loadStudentsFromDatabase();
    }

   private void loadStudentsFromDatabase() {
    System.out.println("Loading students from database...");
    String query = "SELECT id, name, address,subjects, mobile FROM student_information";

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "");
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        studentList.clear(); // ← Clear old data

        while (rs.next()) {
            Student student = new Student(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("subjects"),
                    rs.getString("mobile"));  // ← Now added
            studentList.add(student);
        }

        studentTable.setItems(studentList);
        System.out.println("Total students loaded: " + studentList.size());

    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    @FXML
    private void handleProfileButtonAction(ActionEvent event) throws IOException {
        Parent profileRoot = FXMLLoader.load(getClass().getResource("TeacherProfile.fxml"));
        Scene profileScene = new Scene(profileRoot);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(profileScene);
        window.show();
    }
}
