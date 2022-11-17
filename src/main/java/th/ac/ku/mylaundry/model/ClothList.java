package th.ac.ku.mylaundry.model;

public class ClothList {
    Integer id ;
    String service ;
    String category;
    Integer quantity ;
    Double pricePerUnit;

    public ClothList(Integer id, String service, String category, Integer quantity) {
        this.id = id;
        this.service = service;
        this.category = category;
        this.quantity = quantity;
    }

    public ClothList(String service, String category, Integer quantity, Double pricePerUnit) {
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

    @Override
    public String toString() {
        return "ClothList{" +
                "id=" + id +
                ", service='" + service + '\'' +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", pricePerUnit=" + pricePerUnit +
                '}';
    }
}
