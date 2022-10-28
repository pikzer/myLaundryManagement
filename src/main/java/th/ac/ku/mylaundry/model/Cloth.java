package th.ac.ku.mylaundry.model;

import java.util.ArrayList;

public class Cloth {
    int id ;
    String clothType ;
    Double price ;


    public Cloth(int id, String clothType, Double price) {
        this.id = id;
        this.clothType = clothType;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getClothType() {
        return clothType;
    }

    public Double getPrice() {
        return price;
    }
}
