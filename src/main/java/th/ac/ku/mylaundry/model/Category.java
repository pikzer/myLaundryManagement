package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class Category {
    int id ;
    int service_rate_id;
    String clothType ;
    double addOnPrice ;

    public Category(int id, int service_rate_id, String clothType, double addOnPrice) {
        this.id = id;
        this.service_rate_id = service_rate_id;
        this.clothType = clothType;
        this.addOnPrice = addOnPrice;
    }

    public int getId() {
        return id;
    }

    public int getService_rate_id() {
        return service_rate_id;
    }

    public String getClothType() {
        return clothType;
    }

    public double getAddOnPrice() {
        return addOnPrice;
    }

    public String getPostEmployee(){
        return "service_rate_id="+getService_rate_id()+"&"
                + "clothType="+getClothType() + "&"
                + "addOnPrice="+getAddOnPrice();

    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", service_rate_id=" + service_rate_id +
                ", clothType='" + clothType + '\'' +
                ", addOnPrice=" + addOnPrice +
                '}';
    }
}
