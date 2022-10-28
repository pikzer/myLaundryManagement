package th.ac.ku.mylaundry.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Order {
    Integer id;
    String service ;
    String name ;
    String pickDate ;
    String pickTime ;
    String deliDate ;
    String deliTime ;
    String address ;
    String responder ;
    String deliver ;
    Boolean payStatus ;
    String payMethod ;
    Double pickSerCharge ;
    Double deliSerCharge ;
    Double total;
    String status ;
    Boolean isMemOrder ;


    public Order(String service, String name, String responder, Boolean payStatus, String payMethod, Double total, String status, Boolean isMemOrder) {
        this.service = service;
        this.name = name;
        this.responder = responder;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.total = total;
        this.status = status;
        this.isMemOrder = isMemOrder;
    }

    public Order(String service, String name, String deliDate, String deliTime, String address, String responder, String deliver, Boolean payStatus, String payMethod, Double deliSerCharge, Double total, String status, Boolean isMemOrder) {
        this.service = service;
        this.name = name;
        this.deliDate = deliDate;
        this.deliTime = deliTime;
        this.address = address;
        this.responder = responder;
        this.deliver = deliver;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.deliSerCharge = deliSerCharge;
        this.total = total;
        this.status = status;
        this.isMemOrder = isMemOrder;
    }

    public Order(String service, String name, String pickDate, String pickTime, String deliDate, String deliTime, String address, String responder, String deliver, Boolean payStatus, String payMethod, Double pickSerCharge, Double deliSerCharge, Double total, String status, Boolean isMemOrder) {
        this.service = service;
        this.name = name;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.deliDate = deliDate;
        this.deliTime = deliTime;
        this.address = address;
        this.responder = responder;
        this.deliver = deliver;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.pickSerCharge = pickSerCharge;
        this.deliSerCharge = deliSerCharge;
        this.total = total;
        this.status = status;
        this.isMemOrder = isMemOrder;
    }

    public Order(Integer id, String service, String name, String pickDate, String pickTime, String deliDate, String deliTime, String address, String responder, String deliver, Boolean payStatus, String payMethod, Double pickSerCharge, Double deliSerCharge, Double total, String status, Boolean isMemOrder) {
        this.id = id;
        this.service = service;
        this.name = name;
        this.pickDate = pickDate;
        this.pickTime = pickTime;
        this.deliDate = deliDate;
        this.deliTime = deliTime;
        this.address = address;
        this.responder = responder;
        this.deliver = deliver;
        this.payStatus = payStatus;
        this.payMethod = payMethod;
        this.pickSerCharge = pickSerCharge;
        this.deliSerCharge = deliSerCharge;
        this.total = total;
        this.status = status;
        this.isMemOrder = isMemOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getDeliDate() {
        return deliDate;
    }

    public void setDeliDate(String deliDate) {
        this.deliDate = deliDate;
    }

    public String getDeliTime() {
        return deliTime;
    }

    public void setDeliTime(String deliTime) {
        this.deliTime = deliTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getResponder() {
        return responder;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public Boolean getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Boolean payStatus) {
        this.payStatus = payStatus;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Double getPickSerCharge() {
        return pickSerCharge;
    }

    public void setPickSerCharge(Double pickSerCharge) {
        this.pickSerCharge = pickSerCharge;
    }

    public Double getDeliSerCharge() {
        return deliSerCharge;
    }

    public void setDeliSerCharge(Double deliSerCharge) {
        this.deliSerCharge = deliSerCharge;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getMemOrder() {
        return isMemOrder;
    }

    public void setMemOrder(Boolean memOrder) {
        isMemOrder = memOrder;
    }
}
