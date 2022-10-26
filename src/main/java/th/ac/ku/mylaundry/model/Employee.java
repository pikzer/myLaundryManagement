package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class Employee {
    private Integer id;
    private String name;
    private String phone;
    private String email;
    private String role;
    private String password;
    private Double salary;
    private String address;
    private String idCard;
    private String bankAccountNumber;
    private String bankName;

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

    public Employee(int id, String name, String phone, String email, String role, double salary, String address, String idCard, String bankAccountNumber, String bankName) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.salary = salary;
        this.address = address;
        this.idCard = idCard;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
    }

    public Employee(String name, String phone, String email, String role, String password, Double salary, String address, String idCard, String bankAccountNumber, String bankName) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.password = password;
        this.salary = salary;
        this.address = address;
        this.idCard = idCard;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
    }

    public Employee(Integer id, String name, String phone, String email, String role, String password, Double salary, String address, String idCard, String bankAccountNumber, String bankName) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
        this.password = password;
        this.salary = salary;
        this.address = address;
        this.idCard = idCard;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public double getSalary() {
        return salary;
    }

    public String getAddress() {
        return address;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public String getPostEmployee(){
        return "ownerName="+getName()+"&"
                +"ownerPhone="+ getPhone()+"&"
                +"ownerEmail="+ getEmail()+"&"
                +"password="+ getPassword()+"&"
                +"ownerAddress="+ getAddress()+"&"
                +"ownerBankNum="+ getBankAccountNumber()+"&"
                +"ownerBankName="+ getBankName()+"&"
                +"ownerIdCard="+ getIdCard() ;
    }
}
