package javafxapplication9;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class AdminDashboardController {

    @FXML private TableView<Teacher> teacherTable;
    @FXML private TableColumn<Teacher, Integer> teacherIdCol;
    @FXML private TableColumn<Teacher, String> teacherNameCol;
    @FXML private TableColumn<Teacher, String> teacherAddressCol;
    @FXML private TableColumn<Teacher, String> teacherSubjectCol;
    @FXML private TableColumn<Teacher, String> teacherMobileCol;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, Integer> studentIdCol;
    @FXML private TableColumn<Student, String> studentNameCol;
    @FXML private TableColumn<Student, String> studentAddressCol;
    @FXML private TableColumn<Student, String> studentMobileCol;

    private final ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTeacherTable();
        setupStudentTable();
        loadTeachersFromDB();
        loadStudentsFromDB();
    }

    private void setupTeacherTable() {
        teacherIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        teacherNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        teacherSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subjects"));
        teacherMobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
    }

    private void setupStudentTable() {
        studentIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        studentMobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
    }

    private void loadTeachersFromDB() {
        teacherList.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM teacher_information")) {

            while (rs.next()) {
                teacherList.add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("subjects"),
                        rs.getString("mobile")
                ));
            }
            teacherTable.setItems(teacherList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentsFromDB() {
        studentList.clear();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM student_information")) {

            while (rs.next()) {
                studentList.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("mobile")
                ));
            }
            studentTable.setItems(studentList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditTeacher(ActionEvent event) {
        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Launch edit form with selected data (optional for now)
            System.out.println("Editing Teacher: " + selected.getName());
        }
    }

    @FXML
    private void handleDeleteTeacher(ActionEvent event) {
        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM teacher_information WHERE id = ?")) {

                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
                teacherList.remove(selected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleEditStudent(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Launch edit form with selected data (optional for now)
            System.out.println("Editing Student: " + selected.getName());
        }
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        Student selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/home_tutor", "root", "");
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM student_information WHERE id = ?")) {

                stmt.setInt(1, selected.getId());
                stmt.executeUpdate();
                studentList.remove(selected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // Implement logout logic: return to login page or close window
        System.out.println("Logging out...");
    }
}
