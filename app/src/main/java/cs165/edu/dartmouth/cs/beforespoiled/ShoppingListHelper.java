package cs165.edu.dartmouth.cs.beforespoiled;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Yuzhong on 2016/5/19.
 */

public class ShoppingListHelper extends SQLiteOpenHelper {

	public static final String TABLE_COMMENTS = "ShoppingList";
	public static final String COLUMN_ID = "_id";
	public static final String LIST_NUMBER = "list_number";
	public static final String COLUMN_ITEMNAME = "item_name";
	public static final String COLUMN_ITEMNUMBER = "item_number";


	private static final String DATABASE_NAME = "BeforeSpoiled.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ TABLE_COMMENTS + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ LIST_NUMBER + "INTEGER, "
			+ COLUMN_ITEMNAME + " TEXT NOT NULL, "
			+ COLUMN_ITEMNUMBER + " TEXT NOT NULL);";

	public ShoppingListHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ShoppingListHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
		onCreate(db);
	}

}