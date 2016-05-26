package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;

/**
 * Created by Fanzy on 5/22/16.
 */
public class LabelDataSource {
    // Database fields
    public static final String TABLE_LABEL = "label";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "item_name";
    public static final String COLUMN_STORAGE_PERIOD = "storage_period";
    public static final String COLUMN_DAYS_BEFORE_SPOILED = "days_before_spoiled";

    private SQLiteDatabase database;
    private MyDBHelper dbHelper;

    public LabelDataSource ( Context context){
        dbHelper = new MyDBHelper(context);
        open();
        if (numOfLabel() == 0) {
            String[] labels = context.getResources().getStringArray(R.array.CategorySpinner);
            ContentValues values = new ContentValues();
            for (int i = 0; i < labels.length; i++) {
                values.put(COLUMN_ID, i);
                values.put(COLUMN_NAME, labels[i]);
                values.put(COLUMN_STORAGE_PERIOD, 7);
                values.put(COLUMN_DAYS_BEFORE_SPOILED, 1);
                database.insert(TABLE_LABEL, null, values);
            }
        }
        close();
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public int numOfLabel() {
        Cursor cursor = database.rawQuery("SELECT count(*) FROM " + TABLE_LABEL, null);
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    // Insert an item given each column value
    public long insertEntry(Label entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, entry.getName());
        values.put(COLUMN_STORAGE_PERIOD, entry.getStoragePeriod());
        values.put(COLUMN_DAYS_BEFORE_SPOILED, entry.getStoragePeriod());
        return database.insert(TABLE_LABEL, null,
                values);
    }

    // Remove an entry by giving its index
    public int removeEntry(long rowIndex) {
        return database.delete(TABLE_LABEL, COLUMN_ID
                + " = " + rowIndex, null);
    }

    // Update an entry given each column value
    public int updateEntry(Label entry) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, entry.getId());
        values.put(COLUMN_NAME, entry.getName());
        values.put(COLUMN_STORAGE_PERIOD, entry.getStoragePeriod());
        values.put(COLUMN_DAYS_BEFORE_SPOILED, entry.getDaysBeforeSpoiled());
        return database.update(TABLE_LABEL, values, COLUMN_ID
                + " = " + entry.getId(), null);
    }

    // Query a specific entry by its index.
    public Label fetchEntryByIndex(long rowId) {
        Cursor cursor = database.query(TABLE_LABEL,
                null, COLUMN_ID + " = " + rowId, null,
                null, null, null);
        cursor.moveToFirst();
        Label entry = cursorToLabel(cursor);
        cursor.close();

        return entry;
    }

    // Query the entire table, return all rows
    public List<Label> fetchEntries() {
        List<Label> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_LABEL,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Label entry = cursorToLabel(cursor);
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
    private Label cursorToLabel(Cursor cursor) {
        Label label = new Label();
        label.setId(cursor.getLong(0));
        label.setName(cursor.getString(1));
        label.setDaysBeforeSpoiled(cursor.getInt(2));
        label.setStoragePeriod(cursor.getInt(3));
        return label;
    }

}
