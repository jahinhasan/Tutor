package javafxapplication9;

public class Session {

    private static String studentId;
    private static String studentName; // Optional: for displaying name in dashboard, etc.
    private static String studentEmail; // Optional

    // Set student session info
    public static void setStudentSession(String id, String name, String email) {
        studentId = id;
        studentName = name;
        studentEmail = email;
    }

    // Getters
    public static String getStudentId() {
        return studentId;
    }

    public static String getStudentName() {
        return studentName;
    }

    public static String getStudentEmail() {
        return studentEmail;
    }

    // Clear session when logging out
    public static void clear() {
        studentId = null;
        studentName = null;
        studentEmail = null;
    }
}
