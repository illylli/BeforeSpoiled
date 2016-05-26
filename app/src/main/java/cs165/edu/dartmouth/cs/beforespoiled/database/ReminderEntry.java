package cs165.edu.dartmouth.cs.beforespoiled.database;

import java.util.Calendar;

public class ReminderEntry {
    private long id;
    private String name;
    private Integer label;
    private Calendar expireDate;
    private byte[] image;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public Calendar getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Calendar expireDate) {
        this.expireDate = expireDate;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
