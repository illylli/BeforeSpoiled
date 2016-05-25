package cs165.edu.dartmouth.cs.beforespoiled.database;

import java.util.Date;

/**
 * Created by Yuzhong on 2016/5/22.
 */
public class ShoppingLists {
    private long id;
    private Date date;
//    private String itemName;
//    private int itemNumber;

    public ShoppingLists(){}

    public ShoppingLists(Date date){
        this.date = date;
    }

//    public ShoppingLists(Date date, String itemName, int itemNumber){
//        this.date = date;
//        this.itemName = itemName;
//        this.itemNumber = itemNumber;
//    }

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

//    public String getItemName() {
//        return itemName;
//    }
//
//    public void setItemName(String itemName) {
//        this.itemName = itemName;
//    }
//
//    public int getItemNumber() {
//        return itemNumber;
//    }
//
//    public void setItemNumber(int itemNumber) {
//        this.itemNumber = itemNumber;
//    }
}
