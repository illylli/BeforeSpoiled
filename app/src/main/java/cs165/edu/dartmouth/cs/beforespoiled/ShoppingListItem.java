package cs165.edu.dartmouth.cs.beforespoiled;

/**
 * Created by Shicheng on 2016/5/19.
 */
public class ShoppingListItem {
    private long id;
    private String itemName;
    private int itemNumber;

    public ShoppingListItem(){}

    public ShoppingListItem(String name, int itemNumber){
        this.itemName = name;
        this.itemNumber = itemNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }
}
