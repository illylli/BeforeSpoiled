package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class ReadTemplateFromDataBase extends AsyncTaskLoader<List<TemplateCover>> {
    private static final String TAG = "AsyncTask";
    private TemplateDataSource mDataSource;
    private Context context;

    public ReadTemplateFromDataBase(Context context){
        super(context);
        this.context = context;
        mDataSource = new TemplateDataSource(context);
    }

    @Override
    public List<TemplateCover> loadInBackground() {
        mDataSource.open();
        // read data from database
        List<TemplateCover> list = mDataSource.getAllItems();

        Log.d("Database", "Read All:" + list.size());

        mDataSource.close();
        return list;
    }
}
