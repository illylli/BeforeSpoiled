package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by Shicheng on 2016/5/30.
 */
public class DeleteTemplateFromDatabase extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private long pos;
    TemplateDataSource mDataSource;

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
