package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListDataSource {
	// Database fields
	public static final String TABLE_SHOPPINGLIST = "ShoppingList";
	public static final String COLUMN_ID = "_id";
	public static final String LIST_NUMBER = "list_number";
	public static final String COLUMN_ITEMNAME = "item_name";
	public static final String COLUMN_ITEMNUMBER = "item_number";

	private SQLiteDatabase database;
	private MyDBHelper dbHelper;
	private String[] allColumns = { COLUMN_ID, COLUMN_ITEMNAME,
			COLUMN_ITEMNUMBER};

	public ShoppingListDataSource(Context context) {
		dbHelper = new MyDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Card createHistory(Card card) {
		ContentValues values = new ContentValues();

		//get one historyEntry values
		values.put(COLUMN_ITEMNAME, card.getItemName());
		values.put(COLUMN_ITEMNUMBER, card.getItemNumber());

		long insertId = database.insert(TABLE_SHOPPINGLIST, null,
				values);
		Cursor cursor = database.query(TABLE_SHOPPINGLIST,
				allColumns, COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Card newCard = cursorToShoppingListItem(cursor);

		cursor.close();
		return newCard;
	}

	public void deleteHistory(long id){
		database.delete(TABLE_SHOPPINGLIST, COLUMN_ID
				+ " = " + id, null);
	}
	
	public void deleteAllHistories() {
		System.out.println("HistroyEntries deleted all");
		database.delete(TABLE_SHOPPINGLIST, null, null);
	}

	public List<Card> getAllItems() {
		List<Card> cardList = new ArrayList<>();

		Cursor cursor = database.query(TABLE_SHOPPINGLIST,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Card card = cursorToShoppingListItem(cursor);
			cardList.add(card);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return cardList;
	}

	private Card cursorToShoppingListItem(Cursor cursor) {
		Card card = new Card();

		card.setId(cursor.getLong(0));
		card.setItemName(cursor.getString(1));
		card.setItemNumber(cursor.getInt(2));

		return card;
	}
}