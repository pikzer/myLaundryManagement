package th.ac.ku.mylaundry.model;

import java.time.OffsetDateTime;

public class ServiceRate {
    private long id;
    private String service;
    private long basePrice;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public long getID() { return id; }
    public void setID(long value) { this.id = value; }

    public String getService() { return service; }
    public void setService(String value) { this.service = value; }

    public long getBasePrice() { return basePrice; }
    public void setBasePrice(long value) { this.basePrice = value; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }
}
