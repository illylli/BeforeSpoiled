package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
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
    private boolean deleteimageflag = false;

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
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
            @Override
            public boolean onLongClick(View view) {
                GridView gridView = (GridView) parent;
                ImageButton ib = (ImageButton) (((View) gridView.getParent()).findViewById(R.id.ibtn_reminder_delete));
                int count = gridView.getChildCount();
                if (!deleteimageflag) {
                    for(int i = 0; i < count; i++){
                        gridView.getChildAt(i).findViewById(R.id.ibtn_reminder_grid_delete).setVisibility(View.VISIBLE);
                    }
                    ib.setVisibility(View.VISIBLE);
                    deleteimageflag = true;
                } else {
                    for(int i = 0; i < count; i++){
                        gridView.getChildAt(i).findViewById(R.id.ibtn_reminder_grid_delete).setVisibility(View.INVISIBLE);
                    }
                    ib.setVisibility(View.INVISIBLE);
                    deleteimageflag = false;
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
            badge.setBadgeBackgroundColor(Color.parseColor("#C0CF21"));
        } else {
            badge.setBadgeBackgroundColor(Color.parseColor("#A4C639"));
        }
//        badge.setBackgroundResource(R.drawable.badge_ifaux);
        badge.show();

        // delete button
        ImageButton btnDelete = (ImageButton) gridEntry.findViewById(R.id.ibtn_reminder_grid_delete);
        btnDelete.setVisibility(deleteimageflag ? View.VISIBLE : View.INVISIBLE);
        btnDelete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ReminderEntryAsyncTask(mContext).execute(ReminderEntryAsyncTask.DELETE, getItemId(position));
                badge.hide();
                entries.remove(position);
                if(entries.isEmpty()){
                    deleteimageflag = false;
                    ((View) parent.getParent()).findViewById(R.id.ibtn_reminder_delete).setVisibility(View.INVISIBLE);
                }
                ReminderGridAdapter.this.notifyDataSetChanged();
                ImageButton ib = (ImageButton) gridEntry.findViewById(R.id.ibtn_reminder_grid_delete);
                ib.setVisibility(View.GONE);
            }
        });

        // name
        ((TextView) gridEntry.findViewById(R.id.tv_reminder_grid_name)).setText(entry.getName());


        return gridEntry;
    }

    public boolean isDeleteimageflag() {
        return deleteimageflag;
    }

    public void setDeleteimageflag(boolean deleteimageflag) {
        this.deleteimageflag = deleteimageflag;
    }
}
