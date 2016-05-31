package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;

public class DeleteTemplateFromDatabase extends AsyncTask<Void, Integer, Void> {

    TemplateDataSource mDataSource;
    private Context context;
    private long pos;

    public DeleteTemplateFromDatabase(Context context, long pos){
        this.context = context;
        this.pos = pos;
    }

    @Override
    protected void onPreExecute() {
        // Getting reference to the TextView tv_counter of the layout activity_main
        mDataSource = new TemplateDataSource(context);
    }



    @Override
    protected Void doInBackground(Void... params) {
        mDataSource.open();
        mDataSource.deleteHistory(pos);
        mDataSource.close();
        return null;
    }
}
