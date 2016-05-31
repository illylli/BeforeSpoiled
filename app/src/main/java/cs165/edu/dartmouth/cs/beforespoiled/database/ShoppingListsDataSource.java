package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.helper.DateHelper;

public class ShoppingListsDataSource {
    // Database fields
    public static final String TABLE_SHOPPINGLISTS = "ShoppingLists";
    public static final String COLUMN_ID = "_id";
    public static final String CREATE_DATE = "create_date";
    private SQLiteDatabase database;
    private MyDBHelper dbHelper;
    private String[] allColumns = { COLUMN_ID, CREATE_DATE };

    public ShoppingListsDataSource(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //add one history to database
    public ShoppingLists createHistory(ShoppingLists shoppingLists) {
        ContentValues values = new ContentValues();

        //get one historyEntry values
        values.put(CREATE_DATE, DateHelper.formatDate(shoppingLists.getDate()));

        long insertId = database.insert(TABLE_SHOPPINGLISTS, null,
                values);
        Cursor cursor = database.query(TABLE_SHOPPINGLISTS,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        ShoppingLists newShoppingLists = cursorToShoppingLists(cursor);

        cursor.close();
        return newShoppingLists;
    }

    public List<ShoppingLists> fetchRecentHistory(int count) {
        List<ShoppingLists> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SHOPPINGLISTS,
                null, null, null, null, null, CREATE_DATE + " desc", count + "");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ShoppingLists entry = cursorToShoppingLists(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    public List<ShoppingLists> fetchHistory() {
        List<ShoppingLists> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SHOPPINGLISTS,
                null, null, null, null, null, CREATE_DATE + " desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ShoppingLists entry = cursorToShoppingLists(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    public void deleteHistory(long id){
        database.delete(TABLE_SHOPPINGLISTS, COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllHistories() {
        System.out.println("HistroyEntries deleted all");
        database.delete(TABLE_SHOPPINGLISTS, null, null);
    }

    public List<ShoppingLists> getAllItems() {
        List<ShoppingLists> shoppingLists = new ArrayList<>();

        Cursor cursor = database.query(TABLE_SHOPPINGLISTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ShoppingLists shoppingList = cursorToShoppingLists(cursor);
            shoppingLists.add(shoppingList);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return shoppingLists;
    }

    private ShoppingLists assignToList(ShoppingLists shoppingLists){

        return shoppingLists;
    }

    private ShoppingLists cursorToShoppingLists(Cursor cursor) {
        ShoppingLists shoppingLists = new ShoppingLists();

        shoppingLists.setId(cursor.getLong(0));
        try {
            shoppingLists.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(cursor.getString(1)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return shoppingLists;
    }

}
