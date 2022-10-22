package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class MemberPackage {
    private Integer id;
    private String service;
    private Integer quantity;
    private Double price;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Object deletedAt;

    public MemberPackage(Integer id, String service, Integer quantity, Double price) {
        this.id = id;
        this.service = service;
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getService() {
        return service;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }
}
