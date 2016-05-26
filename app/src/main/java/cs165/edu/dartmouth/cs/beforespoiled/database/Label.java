package cs165.edu.dartmouth.cs.beforespoiled.database;

/**
 * Created by Fanzy on 5/22/16.
 */
public class Label {
    private long id;
    private String name;
    private Integer storagePeriod;
    private Integer daysBeforeSpoiled;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getDaysBeforeSpoiled() {
        return daysBeforeSpoiled;
    }

    public void setDaysBeforeSpoiled(Integer daysBeforeSpoiled) {
        this.daysBeforeSpoiled = daysBeforeSpoiled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStoragePeriod() {
        return storagePeriod;
    }

    public void setStoragePeriod(Integer storagePeriod) {
        this.storagePeriod = storagePeriod;
    }

    @Override
    public String toString() {
        return name;
    }
}
