package th.ac.ku.mylaundry.model;
import java.time.OffsetDateTime;


public class Address {
        private long id;
        private String name;
        private String uCode;
        private String lat;
        private String lng;
        private String hint;
        private String contact;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private Object deletedAt;

        public long getID() { return id; }
        public void setID(long value) { this.id = value; }

        public String getName() { return name; }
        public void setName(String value) { this.name = value; }

        public String getUCode() { return uCode; }
        public void setUCode(String value) { this.uCode = value; }

        public String getLat() { return lat; }
        public void setLat(String value) { this.lat = value; }

        public String getLng() { return lng; }
        public void setLng(String value) { this.lng = value; }

        public String getHint() { return hint; }
        public void setHint(String value) { this.hint = value; }

        public String getContact() { return contact; }
        public void setContact(String value) { this.contact = value; }

        public OffsetDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(OffsetDateTime value) { this.createdAt = value; }

        public OffsetDateTime getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(OffsetDateTime value) { this.updatedAt = value; }

        public Object getDeletedAt() { return deletedAt; }
        public void setDeletedAt(Object value) { this.deletedAt = value; }
}