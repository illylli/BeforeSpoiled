package cs165.edu.dartmouth.cs.beforespoiled;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDataSource {

	// Database fields
	private SQLiteDatabase database;
	private ShoppingListHelper dbHelper;
	private String[] allColumns = { ShoppingListHelper.COLUMN_ID, ShoppingListHelper.COLUMN_ITEMNAME,
			ShoppingListHelper.COLUMN_ITEMNUMBER};

	private static final String TAG = "DBDEMO";

	public ShoppingListDataSource(Context context) {
		dbHelper = new ShoppingListHelper(context);
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

        //get one historyEntry values
        values.put(ShoppingListHelper.COLUMN_ITEMNAME, shoppingListItem.getItemName());
        values.put(ShoppingListHelper.COLUMN_ITEMNUMBER, shoppingListItem.getItemNumber());

		long insertId = database.insert(ShoppingListHelper.TABLE_COMMENTS, null,
				values);
		Cursor cursor = database.query(ShoppingListHelper.TABLE_COMMENTS,
				allColumns, ShoppingListHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		ShoppingListItem newShoppingListItem = cursorToShoppingListItem(cursor);

		// Log the comment stored
		Log.d(TAG, "shoppingListItem = " + cursorToShoppingListItem(cursor).toString()
				+ " insert ID = " + insertId);

		cursor.close();
		return newShoppingListItem;
	}

	public void deleteHistory(long id){
		database.delete(ShoppingListHelper.TABLE_COMMENTS, ShoppingListHelper.COLUMN_ID
				+ " = " + id, null);
	}
	
	public void deleteAllHistories() {
		System.out.println("HistroyEntries deleted all");
		Log.d(TAG, "delete all = ");
		database.delete(ShoppingListHelper.TABLE_COMMENTS, null, null);
	}
	
	public List<ShoppingListItem> getAllItems() {
		List<ShoppingListItem> shoppingListItems = new ArrayList<>();

		Cursor cursor = database.query(ShoppingListHelper.TABLE_COMMENTS,
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

	private ShoppingListItem cursorToShoppingListItem(Cursor cursor) {
		ShoppingListItem shoppingListItem = new ShoppingListItem();

		shoppingListItem.setId(cursor.getLong(0));
		shoppingListItem.setItemName(cursor.getString(1));
		shoppingListItem.setItemNumber(cursor.getInt(2));

		return shoppingListItem;
	}
}