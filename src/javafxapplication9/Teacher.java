package javafxapplication9;
public class Teacher {
    private int id;
    private String name;
    private String address;
    private String subject;
    private String mobile;

    public Teacher(int id, String name, String address, String subject, String mobile) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.subject = subject;
        this.mobile = mobile;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getSubject() { return subject; }
    public String getMobile() { return mobile; }
}
