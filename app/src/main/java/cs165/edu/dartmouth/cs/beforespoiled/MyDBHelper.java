package cs165.edu.dartmouth.cs.beforespoiled;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryDataSource;

/**
 * Created by Yuzhong on 2016/5/19.
 */

public class MyDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BeforeSpoiled.db";
	private static final int DATABASE_VERSION = 1;

	private static MyDBHelper dbHelper;

	// Database creation sql statement
	private static final String SHOPPINGLIST_CREATE = "CREATE TABLE "
			+ ShoppingListDataSource.TABLE_SHOPPINGLIST + "("
			+ ShoppingListDataSource.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ShoppingListDataSource.LIST_NUMBER + "INTEGER, "
			+ ShoppingListDataSource.COLUMN_ITEMNAME + " TEXT NOT NULL, "
			+ ShoppingListDataSource.COLUMN_ITEMNUMBER + " TEXT NOT NULL);";

	private static final String REMINDER_CREATE = "CREATE TABLE IF NOT EXISTS " + ReminderEntryDataSource.TABLE_REMINDER + " (" +
			ReminderEntryDataSource.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			ReminderEntryDataSource.COLUMN_NAME + " TEXT NOT NULL, " +
			ReminderEntryDataSource.COLUMN_LABLE + " TEXT, " +
			ReminderEntryDataSource.COLUMN_EXPIREDATE + " DATETIME NOT NULL, " +
			ReminderEntryDataSource.COLUMN_PHOTO + " BLOB );";


	private MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static MyDBHelper getInstance(Context context){
		if(dbHelper == null){
			dbHelper = new MyDBHelper(context);
		}
		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SHOPPINGLIST_CREATE);
		database.execSQL(REMINDER_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MyDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + ShoppingListDataSource.TABLE_SHOPPINGLIST);
		db.execSQL("DROP TABLE IF EXISTS " + ReminderEntryDataSource.TABLE_REMINDER);
		onCreate(db);
	}

}