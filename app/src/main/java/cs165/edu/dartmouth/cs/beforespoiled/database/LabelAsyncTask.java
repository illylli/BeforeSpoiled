package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class LabelAsyncTask extends AsyncTask<Object, Void, String> {

    public static final int INSERT = 0;
    public static final int DELETE = 1;
    public static final int UPDATE = 2;

    Context context = null;

    public LabelAsyncTask(Context c) {
        context = c;
    }

    // Params
    @Override
    protected String doInBackground(Object... args) {
        Integer request = (Integer) args[0];
        String msg = "";
        LabelDataSource dbHelper = new LabelDataSource(context);
        dbHelper.open();
        if (request == INSERT) {
            long id = dbHelper.insertEntry((Label) args[1]);
            msg = "insert entry " + id + " successfully";
        } else if (request == DELETE) {
            Long id = (Long) args[1];
            dbHelper.removeEntry(id);
            msg = "delete " + id + " successfully";
        } else if (request == UPDATE) {
            dbHelper.updateEntry((Label) args[1]);
            msg = "update entry " + ((Label) args[1]).getId() + " successfully";
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
