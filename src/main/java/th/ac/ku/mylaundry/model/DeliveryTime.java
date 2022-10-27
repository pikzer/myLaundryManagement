package th.ac.ku.mylaundry.model;

import java.util.Date;

public class DeliveryTime {
    Integer id ;
    Date date ;
    String orderName ;
    String job;

    public DeliveryTime(Date date, String orderName, String job) {
        this.date = date;
        this.orderName = orderName;
        this.job = job;
    }

    public Integer getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getJob() {
        return job;
    }
}
