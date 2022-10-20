package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class Category {
    private long id;
    private long serviceRateID;
    private String clothType;
    private long addOnPrice;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Object deletedAt;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public long getServiceRateID() { return serviceRateID; }
    public void setServiceRateID(long value) { this.serviceRateID = value; }

    public String getClothType() { return clothType; }
    public void setClothType(String value) { this.clothType = value; }

    public long getAddOnPrice() { return addOnPrice; }
    public void setAddOnPrice(long value) { this.addOnPrice = value; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

    public Object getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Object value) { this.deletedAt = value; }
}
