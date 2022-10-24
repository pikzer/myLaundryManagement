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

    private String status;

    public Laundry(String name, String phone, String email, String address, String lineID, String workDay, String opentime, String closetime) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.lineID = lineID;
        this.workDay = workDay;
        this.opentime = opentime;
        this.closetime = closetime;
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
        this.status = status ;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getOwner() {
        return owner;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getLineID() {
        return lineID;
    }

    public String getWorkDay() {
        return workDay;
    }

    public String getOpentime() {
        return opentime;
    }

    public String getClosetime() {
        return closetime;
    }

    public Integer getNumOfWork() {
        return numOfWork;
    }

    public String getPostLaundry(){
        return "shopName="+getName()+"&"
                +"shopPhone="+ getPhone()+"&"
                +"shopAddress="+ getAddress()+"&"
                +"lineId="+ getLineID()+"&"
                +"workDay="+ getWorkDay()+"&"
                +"shopEmail="+ getEmail()+"&"
                +"opentime="+ getOpentime()+"&"
                +"closetime="+ getClosetime() ;
    }

}
