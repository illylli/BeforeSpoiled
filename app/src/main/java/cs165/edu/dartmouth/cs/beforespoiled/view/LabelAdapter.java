package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.Label;

/**
 * Created by Fanzy on 5/22/16.
 */
public class LabelAdapter extends ArrayAdapter<Label> {

    private Context context;
    private List<Label> labels;

    public LabelAdapter(Context context, List<Label> labels) {
        super(context, android.R.layout.simple_list_item_1, labels);
        this.context = context;
        this.labels = labels;
    }

    @Override
    public int getCount() {
        return labels.size();
    }

    @Override
    public Label getItem(int position) {
        return labels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return labels.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        ((TextView)convertView.findViewById(android.R.id.text1)).setText(labels.get(position).getName());
        return convertView;
    }
}
