package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class Employee {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String role;
    private String password;
    private Long salary;
    private String address;
    private String idCard;
    private String bankAccountNumber;
    private String bankName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Object deletedAt;

    public Employee(String name, String phone, String email, String password, String address, String idCard, String bankAccountNumber, String bankName) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.address = address;
        this.idCard = idCard;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
    }

    public Long getID() { return id; }
    public void setID(Long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getRole() { return role; }
    public void setRole(String value) { this.role = value; }

    public String getPassword() { return password; }
    public void setPassword(String value) { this.password = value; }

    public Long getSalary() { return salary; }
    public void setSalary(Long value) { this.salary = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public String getIDCard() { return idCard; }
    public void setIDCard(String value) { this.idCard = value; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String value) { this.bankAccountNumber = value; }

    public String getBankName() { return bankName; }
    public void setBankName(String value) { this.bankName = value; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    public Object getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Object value) { this.deletedAt = value; }

    public String getPostEmployee(){
        return "ownerName="+getName()+"&"
                +"ownerPhone="+ getPhone()+"&"
                +"ownerEmail="+ getEmail()+"&"
                +"password="+ getPassword()+"&"
                +"ownerAddress="+ getAddress()+"&"
                +"ownerBankNum="+ getBankAccountNumber()+"&"
                +"ownerBankName="+ getBankName()+"&"
                +"ownerIdCard="+ getIDCard() ;
    }
}
