package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.MyDBHelper;
import cs165.edu.dartmouth.cs.beforespoiled.helper.DateHelper;

/**
 * Created by Fanzy on 5/20/16.
 */
public class ReminderEntryDataSource {
    public static final String TABLE_REMINDER = "ReminderEntry";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LABLE = "label";
    public static final String COLUMN_EXPIREDATE = "expire_date";
    public static final String COLUMN_PHOTO = "photo";

    private MyDBHelper dbHelper;
    private SQLiteDatabase database;

    public ReminderEntryDataSource ( Context context){
        dbHelper = MyDBHelper.getInstance(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        database.close();
        database = null;
    }

    // Insert an item given each column value
    public long insertEntry(ReminderEntry entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entry.getName());
        values.put(COLUMN_LABLE, entry.getLabel());
        values.put(COLUMN_EXPIREDATE, DateHelper.calendarToData(entry.getExpireDate()));
        values.put(COLUMN_PHOTO, entry.getImage());
        return database.insert(TABLE_REMINDER, null,
                values);
    }

    // Remove an entry by giving its index
    public int removeEntry(long rowIndex) {
        return database.delete(TABLE_REMINDER, COLUMN_ID
                + " = " + rowIndex, null);
    }

    // Query a specific entry by its index.
    public ReminderEntry fetchEntryByIndex(long rowId) {
        Cursor cursor = database.query(TABLE_REMINDER,
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

        Cursor cursor = database.query(TABLE_REMINDER,
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
