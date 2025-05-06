/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package javafxapplication9;

import java.sql.*;

public class DBConnector {
    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Load MySQL JDBC Driver
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/home_tutor", // Replace with your DB name
                "root", // Replace with your MySQL username
                ""      // Replace with your MySQL password
            );
            System.out.println("Connected to MySQL successfully.");
        } catch (Exception e) {
            System.out.println("MySQL Connection Error: " + e.getMessage());
        }
        return conn;
    }

  public static Connection getConnection() throws SQLException {
    String url = "jdbc:mysql://localhost:3306/home_tutor"; // ✅ Update with your DB
    String user = "root";
    String password = ""; // your MySQL password if set
    return DriverManager.getConnection(url, user, password);
}

}

