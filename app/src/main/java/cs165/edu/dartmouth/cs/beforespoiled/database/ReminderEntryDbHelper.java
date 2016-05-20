package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.helper.DateHelper;

/**
 * Created by Fanzy on 5/20/16.
 */
public class ReminderEntryDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_ENTRY = "ReminderEntry";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LABLE = "label";
    public static final String COLUMN_EXPIREDATE = "expire_date";
    public static final String COLUMN_PHOTO = "photo";
    private static final String DATABASE_NAME = "BeforeSpoiled.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_ENTRY + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_LABLE + " TEXT, " +
            COLUMN_EXPIREDATE + " DATETIME NOT NULL, " +
            COLUMN_PHOTO + " BLOB );";
    private SQLiteDatabase database;

    // Constructor
    public ReminderEntryDbHelper(Context context) {
        // DATABASE_NAME is, of course the name of the database, which is defined as a tring constant
        // DATABASE_VERSION is the version of database, which is defined as an integer constant
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
    }

    // Create table schema if not exists
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_ENTRY);
        onCreate(db);
    }

    // Insert an item given each column value
    public long insertEntry(ReminderEntry entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entry.getName());
        values.put(COLUMN_LABLE, entry.getLabel());
        values.put(COLUMN_EXPIREDATE, DateHelper.calendarToData(entry.getExpireDate()));
        values.put(COLUMN_PHOTO, entry.getImage());
        return database.insert(TABLE_ENTRY, null,
                values);
    }

    // Remove an entry by giving its index
    public int removeEntry(long rowIndex) {
        return database.delete(TABLE_ENTRY, COLUMN_ID
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public ReminderEntry fetchEntryByIndex(long rowId) {
        Cursor cursor = database.query(TABLE_ENTRY,
                null, COLUMN_ID + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        ReminderEntry entry = cursorToReminderEntry(cursor);
        cursor.close();

        return entry;
    }

    // Query the entire table, return all rows
    public List<ReminderEntry> fetchEntries() {
        List<ReminderEntry> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_ENTRY,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ReminderEntry entry = cursorToReminderEntry(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    /**
     * convert cursor to ExerciseEntry
     *
     * @param cursor cursor
     * @return the instance of ExerciseEntry
     */
    private ReminderEntry cursorToReminderEntry(Cursor cursor) {
        ReminderEntry reminder = new ReminderEntry();
        reminder.setId(cursor.getLong(0));
        reminder.setName(cursor.getString(1));
        reminder.setLabel(cursor.getString(2));
        reminder.setExpireDate(DateHelper.dataToCalendar(cursor.getString(3)));
        reminder.setImage(cursor.getBlob(4));
        return reminder;
    }
}