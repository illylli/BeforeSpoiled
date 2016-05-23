package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.widget.CheckBox;

/**
 * Created by oubai on 5/22/16.
 */
public class Card {
    private long id;
    private String itemName;
    private int itemNumber;
    boolean selected = false;

    public Card() {}

    public Card(String name, int number, boolean selected) {
        this.itemName = name;
        this.itemNumber = number;
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
