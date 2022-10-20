package th.ac.ku.mylaundry.model;

public class Customer {
    private long id;
    private String name;
    private String phone;
    private String email;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }
}
