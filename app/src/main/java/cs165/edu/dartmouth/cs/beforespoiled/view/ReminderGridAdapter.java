package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntry;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryAsyncTask;

/**
 * Created by oubai on 4/13/16.
 */
public class ReminderGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReminderEntry> entries;

    // ReminderGridAdapter for GridView, receive context and the index
    public ReminderGridAdapter(Context c, List<ReminderEntry> entries) {
        mContext = c;
        this.entries = entries;
    }

    // the length of the matrix
    public int getCount() {
        return entries.size();
    }

    // getItem method, not used
    public Object getItem(int position) {
        return entries.get(position);
    }

    // getItemId, not used
    public long getItemId(int position) {
        return (entries.get(position)).getId();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ReminderEntry entry = entries.get(position);
        View gridEntry;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridEntry = inflater.inflate(R.layout.gridview_reminder, null);
        } else {
            gridEntry = convertView;
        }
        if (entry.getImage() != null) {
            ((ImageView) gridEntry.findViewById(R.id.iv_reminder_grid_image)).setImageBitmap(BitmapFactory.decodeByteArray(entry.getImage(), 0, entry.getImage().length));
        }
        gridEntry.findViewById(R.id.ibtn_reminder_grid_delete).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReminderEntryAsyncTask(mContext).execute(ReminderEntryAsyncTask.DELETE, getItemId(position));
                entries.remove(position);
                ReminderGridAdapter.this.notifyDataSetChanged();
            }
        });
        ((TextView) gridEntry.findViewById(R.id.tv_reminder_grid_name)).setText(entry.getName());
        ((TextView) gridEntry.findViewById(R.id.tv_reminder_grid_label)).setText(entry.getLabel());
        return gridEntry;
    }
}
