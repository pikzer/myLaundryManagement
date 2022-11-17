package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class Laundry {
    private Integer id;
    private String name;
    private String phone;
    private String owner;
    private String email;
    private String address;
    private String lineID;
    private String workDay;
    private String opentime;
    private String closetime;
    private Integer numOfWork;
    private String email_pwd;

    private String status;

    public Laundry(String name, String phone, String email, String address, String lineID, String workDay, String opentime, String closetime,String email_pwd) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.lineID = lineID;
        this.workDay = workDay;
        this.opentime = opentime;
        this.closetime = closetime;
        this.email_pwd = email_pwd ;
    }

    public Laundry(Integer id, String name, String phone, String owner, String email, String address, String lineID, String workDay, String opentime, String closetime, Integer numOfWork, String status,String email_pwd) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.owner = owner;
        this.email = email;
        this.address = address;
        this.lineID = lineID;
        this.workDay = workDay;
        this.opentime = opentime;
        this.closetime = closetime;
        this.numOfWork = numOfWork;
        this.email_pwd = email_pwd;
        this.status = status;
    }

    public Laundry(Integer id, String name, String phone, String owner, String email, String address, String lineID, String workDay, String opentime, String closetime, Integer numOfWork, String status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.owner = owner;
        this.email = email;
        this.address = address;
        this.lineID = lineID;
        this.workDay = workDay;
        this.opentime = opentime;
        this.closetime = closetime;
        this.numOfWork = numOfWork;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLineID() {
        return lineID;
    }

    public void setLineID(String lineID) {
        this.lineID = lineID;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public void setClosetime(String closetime) {
        this.closetime = closetime;
    }

    public Integer getNumOfWork() {
        return numOfWork;
    }

    public void setNumOfWork(Integer numOfWork) {
        this.numOfWork = numOfWork;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail_pwd() {
        return email_pwd;
    }

    public String getPostLaundry(){
        return "shopName="+getName()+"&"
                +"shopPhone="+ getPhone()+"&"
                +"shopAddress="+ getAddress()+"&"
                +"lineId="+ getLineID()+"&"
                +"workDay="+ getWorkDay()+"&"
                +"shopEmail="+ getEmail()+"&"
                +"opentime="+ getOpentime()+"&"
                +"closetime="+ getClosetime()+"&"
                +"email_pwd="+ getEmail_pwd();
    }

}
