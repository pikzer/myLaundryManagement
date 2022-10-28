package th.ac.ku.mylaundry.model;

public class ClothList {
    Integer id ;
    String service ;
    String category;
    Integer quantity ;
    Double pricePerUnit;


    public ClothList(String service, String category, Integer quantity,Double pricePerUnit) {
        this.service = service;
        this.category = category;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit ;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public String getCategory() {
        return category;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
