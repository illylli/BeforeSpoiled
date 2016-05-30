package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

public class ReminderEntryAsyncTask extends AsyncTask<Object, Void, String> {

    public static final String REQUEST = "request";
    public static final String DATA = "data";
    public static final int INSERT = 0;
    public static final int DELETE = 1;
    public static final int DELETE_OUT_OF_DATE_FOOD = 2;

    Context context = null;

    public ReminderEntryAsyncTask(Context c) {
        context = c;
    }

    // Params
    @Override
    protected String doInBackground(Object... args) {
        Integer request = (Integer) args[0];
        String msg = "";
        ReminderEntryDataSource dbHelper = new ReminderEntryDataSource(context);
        dbHelper.open();
        if (request == INSERT) {
            long id = dbHelper.insertEntry((ReminderEntry) args[1]);
            msg = "insert entry " + id + " successfully";
        } else if (request == DELETE) {
            Long id = (Long) args[1];
            dbHelper.removeEntry(id);
            msg = "delete " + id + " successfully";
        } else if (request == DELETE_OUT_OF_DATE_FOOD) {
            dbHelper.removeOutOfDateEntry();
            msg = "delete out-of-date foods successfully";
        }
        dbHelper.close();
        return msg;
    }

    // Result
    @Override
    protected void onPostExecute(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
