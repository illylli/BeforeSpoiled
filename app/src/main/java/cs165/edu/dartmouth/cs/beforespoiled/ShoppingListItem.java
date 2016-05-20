package cs165.edu.dartmouth.cs.beforespoiled;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class ShoppingListItem {
    private long id;
    private String itemName;
    private int itemNumber;
    boolean selected = false;

    public ShoppingListItem(){}

    public ShoppingListItem(String name, int itemNumber, boolean selected){
        this.itemName = name;
        this.itemNumber = itemNumber;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
