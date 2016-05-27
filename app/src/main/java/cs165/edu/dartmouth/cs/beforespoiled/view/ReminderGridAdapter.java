package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.LabelDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntry;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryAsyncTask;

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
        final ReminderEntry entry = entries.get(position);
        final View gridEntry;

        LayoutInflater inflater = (LayoutInflater) mContext.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        gridEntry = inflater.inflate(R.layout.gridview_reminder, null);

        if (entry.getImage() != null) {
            gridEntry.findViewById(R.id.iv_reminder_grid_image).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog nagDialog = new Dialog(mContext, android.R.style.Theme_Translucent_NoTitleBar);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCanceledOnTouchOutside(true);
                    nagDialog.setContentView(R.layout.dialog_image);
                    ImageButton btnClose = (ImageButton) nagDialog.findViewById(R.id.btn_reminder_dialog);
                    ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.iv_reminder_dialog);
                    ivPreview.setImageBitmap(BitmapFactory.decodeByteArray(entry.getImage(), 0, entry.getImage().length));

                    ivPreview.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            nagDialog.dismiss();
                        }
                    });
                    btnClose.setOnClickListener(new Button.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                }
            });
        }

        // label image
        LabelDataSource labelDataSource = new LabelDataSource(mContext);
        ((ImageView) gridEntry.findViewById(R.id.iv_reminder_grid_image)).setImageResource(labelDataSource.getImageReSrcById(entry.getLabel()));
        gridEntry.findViewById(R.id.iv_reminder_grid_image).setOnLongClickListener(new View.OnLongClickListener() {
            int deleteimageflag = 0;
            @Override
            public boolean onLongClick(View view) {
                ImageButton ib = (ImageButton) gridEntry.findViewById(R.id.ibtn_reminder_grid_delete);
                if (deleteimageflag == 0) {
                    ib.setVisibility(View.VISIBLE);
                    deleteimageflag = 1;
                } else {
                    ib.setVisibility(View.INVISIBLE);
                    deleteimageflag = 0;
                }
                return true;
            }
        });

        final BadgeView badge = new BadgeView(mContext.getApplicationContext(), gridEntry.findViewById(R.id.iv_reminder_grid_image));
        long days = (entry.getExpireDate().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000 / 3600 / 24;
        if (days < 0) {
            days = 0;
        }
        badge.setText(days + "");
        if (days == 0) {
            badge.setBadgeBackgroundColor(Color.GRAY);
        } else if (days == 1) {
            badge.setBadgeBackgroundColor(Color.RED);
        } else if (days == 2) {
            badge.setBadgeBackgroundColor(Color.YELLOW);
        } else {
            badge.setBadgeBackgroundColor(Color.parseColor("#A4C639"));
        }
//        badge.setBackgroundResource(R.drawable.badge_ifaux);
        badge.show();

        // delete button
        gridEntry.findViewById(R.id.ibtn_reminder_grid_delete).setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReminderEntryAsyncTask(mContext).execute(ReminderEntryAsyncTask.DELETE, getItemId(position));
                badge.hide();
                entries.remove(position);
                ReminderGridAdapter.this.notifyDataSetChanged();
                ImageButton ib = (ImageButton) gridEntry.findViewById(R.id.ibtn_reminder_grid_delete);
                ib.setVisibility(View.GONE);
            }
        });

        // name
        ((TextView) gridEntry.findViewById(R.id.tv_reminder_grid_name)).setText(entry.getName());


        return gridEntry;
    }
}
