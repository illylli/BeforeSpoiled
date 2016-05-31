package cs165.edu.dartmouth.cs.beforespoiled.database;

import java.util.Date;

public class ShoppingLists {
    private long id;
    private Date date;

    public ShoppingLists(){}

    public ShoppingLists(Date date){
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
