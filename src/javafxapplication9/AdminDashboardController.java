package javafxapplication9;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {

    // Student TableView and Columns
    @FXML private TableView<UserModel> studentTable;
    @FXML private TableColumn<UserModel, String> studentNameCol;
    @FXML private TableColumn<UserModel, String> studentMobileCol;
    @FXML private TableColumn<UserModel, String> studentAddressCol;
    @FXML private TableColumn<UserModel, String> studentBioCol;
    @FXML private TableColumn<UserModel, String> studentEducationCol;
    @FXML private TableColumn<UserModel, String> studentSubjectCol;

    // Teacher TableView and Columns
    @FXML private TableView<UserModel> teacherTable;
    @FXML private TableColumn<UserModel, String> teacherNameCol;
    @FXML private TableColumn<UserModel, String> teacherMobileCol;
    @FXML private TableColumn<UserModel, String> teacherAddressCol;
    @FXML private TableColumn<UserModel, String> teacherBioCol;
    @FXML private TableColumn<UserModel, String> teacherEducationCol;
    @FXML private TableColumn<UserModel, String> teacherSubjectCol;

    private Connection connectToDatabase() {
        try {
            String url = "jdbc:mysql://localhost:3306/home_tutor"; // your DB url
            String user = "root"; // your DB user
            String password = ""; // your DB password

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            showAlert("Database Connection Failed", e.getMessage());
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupStudentTable();
        setupTeacherTable();
    }

    private void setupStudentTable() {
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentMobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        studentAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        studentBioCol.setCellValueFactory(new PropertyValueFactory<>("bio"));
        studentEducationCol.setCellValueFactory(new PropertyValueFactory<>("education"));
        studentSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));

        studentTable.setItems(loadUserData("student"));
    }

    private void setupTeacherTable() {
        teacherNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        teacherMobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        teacherAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        teacherBioCol.setCellValueFactory(new PropertyValueFactory<>("bio"));
        teacherEducationCol.setCellValueFactory(new PropertyValueFactory<>("education"));
        teacherSubjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));

        teacherTable.setItems(loadUserData("teacher"));
    }

    private ObservableList<UserModel> loadUserData(String userType) {
        ObservableList<UserModel> dataList = FXCollections.observableArrayList();

        String infoTable = userType + "_information";

        // Assuming name is PRIMARY KEY or UNIQUE (adjust if needed)
        String query = "SELECT name, mobile, address, bio, education, subjects AS subject FROM " + infoTable;

        try (Connection conn = connectToDatabase();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                dataList.add(new UserModel(
                        rs.getString("name"),
                        rs.getString("mobile"),
                        rs.getString("address"),
                        rs.getString("bio"),
                        rs.getString("education"),
                        rs.getString("subject")
                ));
            }
        } catch (SQLException e) {
            showAlert("Database Query Error", e.getMessage());
        }

        return dataList;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    // EDIT and DELETE for Student

    @FXML
    private void handleEditStudent(ActionEvent event) {
        UserModel selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to edit.");
            return;
        }

        // Open a dialog or simple prompt for editing (for demo, we'll use TextInputDialog)
        TextInputDialog dialog = new TextInputDialog(selected.getMobile());
        dialog.setTitle("Edit Student Mobile");
        dialog.setHeaderText("Edit Mobile for " + selected.getName());
        dialog.setContentText("Enter new mobile:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newMobile = result.get();

            try (Connection conn = connectToDatabase()) {
                String sql = "UPDATE student_information SET mobile = ? WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, newMobile);
                ps.setString(2, selected.getName());

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    selected.setMobile(newMobile);
                    studentTable.refresh();
                    showAlert("Success", "Student mobile updated successfully.");
                }
            } catch (SQLException e) {
                showAlert("Update Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        UserModel selected = studentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a student to delete.");
            return;
        }

        boolean confirmed = showConfirmation("Delete Student",
                "Are you sure you want to delete student: " + selected.getName() + "?");

        if (confirmed) {
            try (Connection conn = connectToDatabase()) {
                String sql = "DELETE FROM student_information WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, selected.getName());

                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    studentTable.getItems().remove(selected);
                    showAlert("Deleted", "Student deleted successfully.");
                }
            } catch (SQLException e) {
                showAlert("Delete Error", e.getMessage());
            }
        }
    }

    // EDIT and DELETE for Teacher

    @FXML
    private void handleEditTeacher(ActionEvent event) {
        UserModel selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a teacher to edit.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog(selected.getMobile());
        dialog.setTitle("Edit Teacher Mobile");
        dialog.setHeaderText("Edit Mobile for " + selected.getName());
        dialog.setContentText("Enter new mobile:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String newMobile = result.get();

            try (Connection conn = connectToDatabase()) {
                String sql = "UPDATE teacher_information SET mobile = ? WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, newMobile);
                ps.setString(2, selected.getName());

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    selected.setMobile(newMobile);
                    teacherTable.refresh();
                    showAlert("Success", "Teacher mobile updated successfully.");
                }
            } catch (SQLException e) {
                showAlert("Update Error", e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteTeacher(ActionEvent event) {
        UserModel selected = teacherTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a teacher to delete.");
            return;
        }

        boolean confirmed = showConfirmation("Delete Teacher",
                "Are you sure you want to delete teacher: " + selected.getName() + "?");

        if (confirmed) {
            try (Connection conn = connectToDatabase()) {
                String sql = "DELETE FROM teacher_information WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, selected.getName());

                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    teacherTable.getItems().remove(selected);
                    showAlert("Deleted", "Teacher deleted successfully.");
                }
            } catch (SQLException e) {
                showAlert("Delete Error", e.getMessage());
            }
        }
    }

    // LOGOUT

    @FXML
    private void handleLogout(ActionEvent event) {
        // Close current window and maybe open login window
        // For now just show info and exit app (you can customize)

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Logout");
        alert.setHeaderText(null);
        alert.setContentText("Logging out... Application will close.");
        alert.showAndWait();

        System.exit(0);
    }
}
