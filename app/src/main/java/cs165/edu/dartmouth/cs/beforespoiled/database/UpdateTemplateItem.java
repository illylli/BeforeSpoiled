package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Yuzhong on 2016/5/30.
 */
public class UpdateTemplateItem extends AsyncTask<Void, Integer, Void> {
    private TemplateDataSource mDataSource;
    private TemplateCover templateCover;
    private Context context;

    public UpdateTemplateItem(Context context, TemplateCover templateCover){
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
    protected Void doInBackground(Void... params) {
        mDataSource.open();
        mDataSource.updateHistory(templateCover);

        mDataSource.close();
        return null;
    }

    // A callback method executed on UI thread, invoked by the publishProgress()
    // from doInBackground() method

    // Overrider this handler to post interim updates to the UI thread. This handler receives the set of parameters
    // passed in publishProgress from within doInbackground.
}