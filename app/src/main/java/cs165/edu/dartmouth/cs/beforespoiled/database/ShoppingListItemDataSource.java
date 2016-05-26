package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListItemDataSource {
    // Database fields
    public static final String TABLE_SHOPPINGLISTITEM = "ShoppingListItem";
    public static final String COLUMN_ID = "_id";
    public static final String LIST_NUMBER = "list_number";
    public static final String COLUMN_ITEMNAME = "item_name";
    public static final String COLUMN_ITEMNUMBER = "item_number";
    public static final String COLUMN_CHECKED = "checked";
    private static final String TAG = "DBDEMO";
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;
    private String[] allColumns = { COLUMN_ID, LIST_NUMBER, COLUMN_ITEMNAME,
            COLUMN_ITEMNUMBER, COLUMN_CHECKED};

    public ShoppingListItemDataSource(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //add one history to database
    public ShoppingListItem createHistory(ShoppingListItem shoppingListItem) {
        ContentValues values = new ContentValues();

        Log.d("Create", "Create " +shoppingListItem.getListId()+ "@" +shoppingListItem.getItemName()+" * " + shoppingListItem.getItemNumber() + "&"+ shoppingListItem.isSelected());
        //get one historyEntry values
        values.put(LIST_NUMBER, shoppingListItem.getListId());
        values.put(COLUMN_ITEMNAME, shoppingListItem.getItemName());
        values.put(COLUMN_ITEMNUMBER, shoppingListItem.getItemNumber());
        boolean check = shoppingListItem.isSelected();
        int number = 0;
        if(check) number = 1;
        values.put(COLUMN_CHECKED, number);

        long insertId = database.insert(TABLE_SHOPPINGLISTITEM, null,
                values);
        Cursor cursor = database.query(TABLE_SHOPPINGLISTITEM,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ShoppingListItem newShoppingListItem = cursorToShoppingListItem(cursor);

        // Log the comment stored
        Log.d(TAG, "shoppingListItem = " + cursorToShoppingListItem(cursor).toString()
                + " insert ID = " + insertId);

        cursor.close();
        return newShoppingListItem;
    }

    //add one history to database
    public void updateHistory(ShoppingListItem shoppingListItem) {
        ContentValues values = new ContentValues();

        Log.d("Create", "Update " + shoppingListItem.getListId()+ "@" +shoppingListItem.getItemName()+" * " + shoppingListItem.getItemNumber() + "&"+ shoppingListItem.isSelected());
        //get one historyEntry values
        values.put(LIST_NUMBER, shoppingListItem.getListId());
        values.put(COLUMN_ITEMNAME, shoppingListItem.getItemName());
        values.put(COLUMN_ITEMNUMBER, shoppingListItem.getItemNumber());
        boolean check = shoppingListItem.isSelected();
        int number = 0;
        if(check) number = 1;
        values.put(COLUMN_CHECKED, number);

        int a = database.update(TABLE_SHOPPINGLISTITEM, values, "_id=" + shoppingListItem.getId(), null);
        Log.d("debug", "Already" + shoppingListItem.getItemName() + " gggg " + shoppingListItem.getListId() + "@@@" + a);
        Log.d("ITEM", "Update" + shoppingListItem.getListId());
    }

    public void deleteHistory(long id){
        database.delete(TABLE_SHOPPINGLISTITEM, COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllHistories() {
        System.out.println("HistroyEntries deleted all");
        Log.d(TAG, "delete all = ");
        database.delete(TABLE_SHOPPINGLISTITEM, null, null);
    }

    public List<ShoppingListItem> getAllItems() {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SHOPPINGLISTITEM,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ShoppingListItem shoppingListItem = cursorToShoppingListItem(cursor);
            Log.d(TAG, "get comment = " + cursorToShoppingListItem(cursor).toString());
            shoppingListItems.add(shoppingListItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return shoppingListItems;
    }

    public List<ShoppingListItem> getItems() {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SHOPPINGLISTITEM,
                allColumns, LIST_NUMBER + " = ?", new String[]{ String.valueOf(-1) }, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ShoppingListItem shoppingListItem = cursorToShoppingListItem(cursor);
            Log.d(TAG, "get comment = " + cursorToShoppingListItem(cursor).toString());
            shoppingListItems.add(shoppingListItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return shoppingListItems;
    }

    public List<ShoppingListItem> getItemsByList(long listId) {
        List<ShoppingListItem> shoppingListItems = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SHOPPINGLISTITEM,
                null, LIST_NUMBER + " = ?", new String[]{String.valueOf(listId)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ShoppingListItem shoppingListItem = cursorToShoppingListItem(cursor);
            shoppingListItems.add(shoppingListItem);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return shoppingListItems;
    }

    private ShoppingListItem assignToList(ShoppingListItem shoppingListItem){

        return shoppingListItem;
    }

    private ShoppingListItem cursorToShoppingListItem(Cursor cursor) {
        ShoppingListItem shoppingListItem = new ShoppingListItem();

        Log.d("Cursor", cursor.getLong(0)+ " @ " +cursor.getLong(1)+ " # " +cursor.getString(2) + " $ "+cursor.getInt(3));
        shoppingListItem.setId(cursor.getLong(0));
        shoppingListItem.setListId(cursor.getLong(1));
        shoppingListItem.setItemName(cursor.getString(2));
        shoppingListItem.setItemNumber(cursor.getInt(3));
        int check = cursor.getInt(4);
        if(check == 1) shoppingListItem.setSelected(true);
        else shoppingListItem.setSelected(false);
        return shoppingListItem;
    }
}