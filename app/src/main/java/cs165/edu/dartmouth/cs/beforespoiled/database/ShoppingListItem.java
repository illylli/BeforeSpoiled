package cs165.edu.dartmouth.cs.beforespoiled.database;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class ShoppingListItem {
    private long id;
    private long listId;
    private String itemName;
    private int itemNumber;
    boolean selected = false;

    public ShoppingListItem(){
        this.listId = -1;
        this.itemNumber = 1;
        this.selected = false;
    }

    public ShoppingListItem(String name, int itemNumber, boolean selected){
        this.listId = -1;
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

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
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
