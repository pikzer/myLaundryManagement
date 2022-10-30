package th.ac.ku.mylaundry.model;

import java.util.Date;

public class DeliveryTime {
    Integer id ;
    String date ;
    String time ;
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
}
