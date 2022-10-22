package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class Laundry {
    private Long id;
    private String name;
    private String phone;
    private String owner;
    private String email;
    private String address;
    private String lineID;
    private String workDay;
    private String opentime;
    private String closetime;
    private Long numOfWork;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

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

    public Long getID() { return id; }
    public void setID(Long value) { this.id = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getPhone() { return phone; }
    public void setPhone(String value) { this.phone = value; }

    public String getOwner() { return owner; }
    public void setOwner(String value) { this.owner = value; }

    public String getEmail() { return email; }
    public void setEmail(String value) { this.email = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public String getLineID() { return lineID; }
    public void setLineID(String value) { this.lineID = value; }

    public String getWorkDay() { return workDay; }
    public void setWorkDay(String value) { this.workDay = value; }

    public String getOpentime() { return opentime; }
    public void setOpentime(String value) { this.opentime = value; }

    public String getClosetime() { return closetime; }
    public void setClosetime(String value) { this.closetime = value; }

    public Long getNumOfWork() { return numOfWork; }
    public void setNumOfWork(Long value) { this.numOfWork = value; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

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
