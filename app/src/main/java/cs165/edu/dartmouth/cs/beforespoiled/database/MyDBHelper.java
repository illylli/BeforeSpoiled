package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "BeforeSpoiled.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String SHOPPINGLISTITEM_CREATE = "CREATE TABLE "
			+ ShoppingListItemDataSource.TABLE_SHOPPINGLISTITEM + "("
			+ ShoppingListItemDataSource.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ShoppingListItemDataSource.LIST_NUMBER + " INTEGER, "
			+ ShoppingListItemDataSource.COLUMN_ITEMNAME + " TEXT NOT NULL, "
			+ ShoppingListItemDataSource.COLUMN_ITEMNUMBER + " INTEGER, "
			+ ShoppingListItemDataSource.COLUMN_CHECKED + " INTEGER NOT NULL DEFAULT 0);";

	private static final String REMINDER_CREATE = "CREATE TABLE IF NOT EXISTS " + ReminderEntryDataSource.TABLE_REMINDER + " (" +
			ReminderEntryDataSource.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			ReminderEntryDataSource.COLUMN_NAME + " TEXT NOT NULL, " +
			ReminderEntryDataSource.COLUMN_LABLE + " INTEGER, " +
			ReminderEntryDataSource.COLUMN_EXPIREDATE + " DATETIME NOT NULL, " +
			ReminderEntryDataSource.COLUMN_PHOTO + " BLOB );";

	private static final String SHOPPINGLISTS_CREATE = "CREATE TABLE "
			+ ShoppingListsDataSource.TABLE_SHOPPINGLISTS + "("
			+ ShoppingListsDataSource.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ ShoppingListsDataSource.CREATE_DATE + " TEXT NOT NULL);";

	private static final String LABEL_CREATE = "CREATE TABLE IF NOT EXISTS " + LabelDataSource.TABLE_LABEL + " (" +
			LabelDataSource.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			LabelDataSource.COLUMN_NAME + " TEXT NOT NULL, " +
			LabelDataSource.COLUMN_STORAGE_PERIOD + " INTEGER NOT NULL, " +
			LabelDataSource.COLUMN_DAYS_BEFORE_SPOILED + " INTEGER);";

	public MyDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SHOPPINGLISTITEM_CREATE);
		database.execSQL(REMINDER_CREATE);
		database.execSQL(SHOPPINGLISTS_CREATE);
		database.execSQL(LABEL_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MyDBHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + ShoppingListItemDataSource.TABLE_SHOPPINGLISTITEM);
		db.execSQL("DROP TABLE IF EXISTS " + ReminderEntryDataSource.TABLE_REMINDER);
		db.execSQL("DROP TABLE IF EXISTS " + ShoppingListsDataSource.TABLE_SHOPPINGLISTS);
		db.execSQL("DROP TABLE IF EXISTS " + LabelDataSource.TABLE_LABEL);
		onCreate(db);
	}

}