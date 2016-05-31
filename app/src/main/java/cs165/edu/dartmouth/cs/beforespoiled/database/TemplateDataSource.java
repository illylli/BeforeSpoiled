package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class TemplateDataSource {
    // Database fields
    public static final String TABLE_TEMPLATE = "Template";
    public static final String COLUMN_ID = "_id";
    public static final String TEMPLATE_NAME = "template_name";
    public static final String TEMPLATE_IMAGE = "template_image";
    public static final String TEMPLATE_DES = "template_des";
    public static final String TEMPLATE_ITEMS = "template_items";

    private SQLiteDatabase database;
    private MyDBHelper dbHelper;
    private String[] allColumns = { COLUMN_ID, TEMPLATE_NAME, TEMPLATE_IMAGE, TEMPLATE_DES, TEMPLATE_ITEMS};

    public TemplateDataSource(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //add one history to database
    public TemplateCover createHistory(TemplateCover templateCover) {
        ContentValues values = new ContentValues();

        //get one historyEntry values
        values.put(TEMPLATE_NAME, templateCover.getTemplateName());
        values.put(TEMPLATE_IMAGE, templateCover.getPhotoId());
        values.put(TEMPLATE_DES, templateCover.getTemplateDes());
        String list = templateCover.getItemsGson();

        if(list != null) values.put(TEMPLATE_ITEMS, list);

        long insertId = database.insert(TABLE_TEMPLATE, null,
                values);
        Cursor cursor = database.query(TABLE_TEMPLATE,
                allColumns, COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        TemplateCover newTemplateCover = cursorToTemplateCover(cursor);

        cursor.close();
        return newTemplateCover;
    }


    //add one history to database
    public void updateHistory(TemplateCover templateCover) {
        ContentValues values = new ContentValues();

        //get one historyEntry values
        values.put(TEMPLATE_NAME, templateCover.getTemplateName());
        values.put(TEMPLATE_IMAGE, templateCover.getPhotoId());
        values.put(TEMPLATE_DES, templateCover.getTemplateDes());
        String list = templateCover.getItemsGson();

        if(list != null) values.put(TEMPLATE_ITEMS, list);

        int a = database.update(TABLE_TEMPLATE, values, "_id=" + templateCover.getId(), null);
    }

    public List<TemplateCover> fetchRecentHistory(int count) {
        List<TemplateCover> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_TEMPLATE,
                null, null, null, null, null, COLUMN_ID + " desc", count + "");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TemplateCover entry = cursorToTemplateCover(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    public List<TemplateCover> fetchHistory() {
        List<TemplateCover> entries = new ArrayList<>();

        Cursor cursor = database.query(TABLE_TEMPLATE,
                null, null, null, null, null, COLUMN_ID + " desc");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TemplateCover entry = cursorToTemplateCover(cursor);
            entries.add(entry);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return entries;
    }

    public void deleteHistory(long id){
        database.delete(TABLE_TEMPLATE, COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllHistories() {
        System.out.println("HistroyEntries deleted all");
        database.delete(TABLE_TEMPLATE, null, null);
    }

    public List<TemplateCover> getAllItems() {
        List<TemplateCover> templateCoverList = new ArrayList<>();

        Cursor cursor = database.query(TABLE_TEMPLATE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TemplateCover templateCover = cursorToTemplateCover(cursor);
            templateCoverList.add(templateCover);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        return templateCoverList;
    }

    private TemplateCover cursorToTemplateCover(Cursor cursor) {
        TemplateCover templateCover = new TemplateCover();

        templateCover.setId(cursor.getLong(0));
        templateCover.setTemplateName(cursor.getString(1));
        templateCover.setPhotoId(cursor.getInt(2));
        templateCover.setTemplateDes(cursor.getString(3));
        String list = cursor.getString(4);
        if(list != null){
            templateCover.setItemsFromGson(list);
        }

        return templateCover;
    }
}
