package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.Label;
import cs165.edu.dartmouth.cs.beforespoiled.database.LabelDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.view.LabelAdapter;

public class LabelsSettingActivity extends ListActivity implements LoaderManager.LoaderCallbacks<List<Label>> {

    private List<Label> labels = new ArrayList<>();
    private ArrayAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labels_setting);
        adapter = new LabelAdapter(this, labels);
        setListAdapter(adapter);
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        SingleLabelFragment singleFragment = SingleLabelFragment.newInstance(labels.get(position));
        singleFragment.show(getFragmentManager(), "EDIT LABEL");
    }

    @Override
    public Loader<List<Label>> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Label>> loader, List<Label> entries) {
        labels.clear();
        labels.addAll(entries);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Label>> loader) {
        labels.clear();
    }

    // reload the data shown in ReminderFragment
    public void reloadData() {
        getLoaderManager().restartLoader(0, null, this).forceLoad();
    }

    // create DataLoader with AsyncTaskLoader
    public static class DataLoader extends AsyncTaskLoader<List<Label>> {
        Context context;

        public DataLoader(Context context) {
            super(context);
            this.context = context;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<Label> loadInBackground() {
            LabelDataSource dbHelper = new LabelDataSource(context);
            dbHelper.open();
            List<Label> result = dbHelper.fetchEntries();
            dbHelper.close();
            return result;
        }
    }

}
