package javafxapplication9;

public class UserModel {
    private String name;
    private String mobile;
    private String address;
    private String bio;
    private String education;
    private String subject;

    public UserModel(String name, String mobile, String address,
                     String bio, String education, String subject) {
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.bio = bio;
        this.education = education;
        this.subject = subject;
    }

    // Getters for PropertyValueFactory
    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }

    public String getBio() {
        return bio;
    }

    public String getEducation() {
        return education;
    }

    public String getSubject() {
        return subject;
    }

    void setMobile(String newMobile) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
