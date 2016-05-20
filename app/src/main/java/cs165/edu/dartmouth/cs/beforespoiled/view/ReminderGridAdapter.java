package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import cs165.edu.dartmouth.cs.beforespoiled.R;

/**
 * Created by oubai on 4/13/16.
 */
public class ReminderGridAdapter extends BaseAdapter {

    private Context mContext;

    // ReminderGridAdapter for GridView, receive context and the index
    public ReminderGridAdapter(Context c) {
        mContext = c;
    }

    // the length of the matrix
    public int getCount() {
        return 9;
    }

    // getItem method, not used
    public Object getItem(int position) {
        return null;
    }

    // getItemId, not used
    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        View gridEntry;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridEntry = inflater.inflate(R.layout.gridview_reminder, null);
        } else {
            gridEntry = convertView;
        }
        ((ImageButton) gridEntry.findViewById(R.id.ibtn_reminder_grid_delete)).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Fanzy", "delete" + position);
            }
        });
        return gridEntry;
    }
}
