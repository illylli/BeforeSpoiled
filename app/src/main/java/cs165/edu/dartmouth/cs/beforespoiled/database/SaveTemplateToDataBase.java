package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class SaveTemplateToDataBase extends AsyncTask<Void, Integer, TemplateCover> {
    private TemplateDataSource mDataSource;
    private TemplateCover templateCover;
    private TemplateCover newTemplateCover;
    private Context context;

    public SaveTemplateToDataBase(Context context, TemplateCover templateCover){
        this.context = context;
        this.templateCover = templateCover;
    }
    // A callback method executed on UI thread on starting the task
    @Override
    protected void onPreExecute() {
        // Getting reference to the TextView tv_counter of the layout activity_main
        mDataSource = new TemplateDataSource(context);
    }

    // A callback method executed on non UI thread, invoked after
    // onPreExecute method if exists

    // Takes a set of parameters of the type defined in your class implementation. This method will be
    // executed on the background thread, so it must not attempt to interact with UI objects.
    @Override
    protected TemplateCover doInBackground(Void... params) {
        mDataSource.open();
        newTemplateCover = mDataSource.createHistory(templateCover);

        mDataSource.close();
        return newTemplateCover;
    }

    @Override
    protected void onPostExecute(TemplateCover templateCover) {
        super.onPostExecute(templateCover);
        context.sendBroadcast(new Intent("AddTemplate"));
    }
}
