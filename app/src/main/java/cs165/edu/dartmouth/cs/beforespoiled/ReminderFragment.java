package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntry;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryAsyncTask;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.view.ReminderGridAdapter;

public class ReminderFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<ReminderEntry>> {

    private List<ReminderEntry> entries = new ArrayList<>();
    private ReminderGridAdapter gridAdapter = null;
    private GridView gridView = null;

    public ReminderFragment() {

    }

    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        gridView = (GridView) view.findViewById(R.id.gv_reminder);
        gridAdapter = new ReminderGridAdapter(getActivity(), entries);
        gridView.setAdapter(gridAdapter);

        view.findViewById(R.id.btn_reminder_add_manual).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ManualAddItemActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.btn_reminder_add_history).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddFromHistoryActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.ibtn_reminder_delete).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridAdapter.setDeleteimageflag(false);
                view.setVisibility(View.INVISIBLE);
                //delete entry in UI
                List<ImageButton> buttons = new ArrayList<ImageButton>();
                for(int i = 0; i < entries.size(); i++){
                    ImageButton tmp = (ImageButton) gridView.getChildAt(i).findViewById(R.id.ibtn_reminder_grid_delete);
                    tmp.setVisibility(View.INVISIBLE);
                    if(entries.get(i).getExpireDate().before(Calendar.getInstance())){
                        buttons.add(tmp);
                    }
                }
                for(ImageButton btn : buttons){
                    btn.performClick();
                }
                //remove from database
                (new ReminderEntryAsyncTask(getActivity())).execute(ReminderEntryAsyncTask.DELETE_OUT_OF_DATE_FOOD);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        return view;
    }

    @Override
    public Loader<List<ReminderEntry>> onCreateLoader(int i, Bundle bundle) {
        return new DataLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ReminderEntry>> loader, List<ReminderEntry> exerciseEntries) {
        entries.clear();
        entries.addAll(exerciseEntries);
        gridAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<ReminderEntry>> loader) {
        entries.clear();
    }

    // reload the data shown in ReminderFragment
    public void reloadData() {
        getLoaderManager().getLoader(0).onContentChanged();
    }

    // create DataLoader with AsyncTaskLoader
    public static class DataLoader extends AsyncTaskLoader<List<ReminderEntry>> {
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
        public List<ReminderEntry> loadInBackground() {
            ReminderEntryDataSource dbHelper = new ReminderEntryDataSource(context);
            dbHelper.open();
            List<ReminderEntry> result = dbHelper.fetchEntries();
            dbHelper.close();
            return result;
        }
    }

}
