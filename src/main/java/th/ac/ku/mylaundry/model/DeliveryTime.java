package th.ac.ku.mylaundry.model;

import java.util.Date;

public class DeliveryTime {
    Integer id ;
    String date ;
    String time ;
    String u_code ;
    String orderName ;
    String deliver ;
    String job;

    public DeliveryTime(Integer id, String date, String time, String orderName, String deliver, String job) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.orderName = orderName;
        this.deliver = deliver;
        this.job = job;
    }

    public DeliveryTime(Integer id, String date, String time, String orderName, String deliver, String job,String u_code) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.u_code = u_code;
        this.orderName = orderName;
        this.deliver = deliver;
        this.job = job;
    }

    public DeliveryTime(String date, String time, String orderName, String job) {
        this.date = date;
        this.time = time;
        this.orderName = orderName;
        this.job = job;
    }

    public DeliveryTime(String date, String time, String orderName, String job, String u_code) {
        this.date = date;
        this.time = time;
        this.u_code = u_code;
        this.orderName = orderName;
        this.job = job;
    }

    public String getDeliver() {
        return deliver;
    }

    public String getTime() {
        return time;
    }

    public Integer getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getJob() {
        return job;
    }

    public String getU_code() {
        return u_code;
    }

    @Override
    public String toString() {
        return "DeliveryTime{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", orderName='" + orderName + '\'' +
                ", deliver='" + deliver + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
