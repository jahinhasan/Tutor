package javafxapplication9;

public class Student {
    private int id;
    private String name;
    private String address;
    private String subjects;
    private String mobile;

    public Student(int id, String name, String address, String subjects) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.subjects = subjects;
        this.mobile = mobile;
    }

    // Getters for PropertyValueFactory
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getSubjects() { return subjects; }
    public String getMobile() { return mobile; }
}
