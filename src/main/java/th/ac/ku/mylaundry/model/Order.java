package th.ac.ku.mylaundry.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Order {
    private long id;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String service;
    private String name;
    private LocalDate pickDate;
    private String pickTime;
    private LocalDate deliDate;
    private String deliTime;
    private String address;
    private String responder;
    private Object deliver;
    private long payStatus;
    private String payMethod;
    private long pickSerCharge;
    private long deliSerCharge;
    private long total;
    private String status;
    private long isMembershipOr;
    private String cusPhone;
    private long employeeID;
    private Object deletedAt;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    public String getService() { return service; }
    public void setService(String value) { this.service = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public LocalDate getPickDate() { return pickDate; }
    public void setPickDate(LocalDate value) { this.pickDate = value; }

    public String getPickTime() { return pickTime; }
    public void setPickTime(String value) { this.pickTime = value; }

    public LocalDate getDeliDate() { return deliDate; }
    public void setDeliDate(LocalDate value) { this.deliDate = value; }

    public String getDeliTime() { return deliTime; }
    public void setDeliTime(String value) { this.deliTime = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public String getResponder() { return responder; }
    public void setResponder(String value) { this.responder = value; }

    public Object getDeliver() { return deliver; }
    public void setDeliver(Object value) { this.deliver = value; }

    public long getPayStatus() { return payStatus; }
    public void setPayStatus(long value) { this.payStatus = value; }

    public String getPayMethod() { return payMethod; }
    public void setPayMethod(String value) { this.payMethod = value; }

    public long getPickSerCharge() { return pickSerCharge; }
    public void setPickSerCharge(long value) { this.pickSerCharge = value; }

    public long getDeliSerCharge() { return deliSerCharge; }
    public void setDeliSerCharge(long value) { this.deliSerCharge = value; }

    public long getTotal() { return total; }
    public void setTotal(long value) { this.total = value; }

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public long getIsMembershipOr() { return isMembershipOr; }
    public void setIsMembershipOr(long value) { this.isMembershipOr = value; }

    public String getCusPhone() { return cusPhone; }
    public void setCusPhone(String value) { this.cusPhone = value; }

    public long getEmployeeID() { return employeeID; }
    public void setEmployeeID(long value) { this.employeeID = value; }

    public Object getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Object value) { this.deletedAt = value; }
}
