package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class ServiceRate {
    int id ;
    String service ;
    double basePrice ;

    public ServiceRate(int id, String service, double basePrice) {
        this.id = id;
        this.service = service;
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
