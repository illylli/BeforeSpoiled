package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;

/**
 * Created by Yuzhong on 2016/5/28.
 */
public class TemplateItemAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> list;

    public TemplateItemAdapter(Context context, List<String> itemList) {
        super(context, R.layout.activity_template_item, itemList);
        Log.d("View", "Hello + +");
        this.context = context;
        this.list = itemList;
    }

    @Override
    public void add(String card) {
        list.add(card);
        super.add(card);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public String getItem(int index) {
        return this.list.get(index);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_template_item, null);
        }
        ((TextView)convertView.findViewById(R.id.textViewItem)).setText(list.get(position));
        Button b = (Button) convertView.findViewById(R.id.delete_template_item);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent("DeleteItem");
                i.putExtra("itemPosition", position);
                context.sendBroadcast(i);
            }
        });
        return convertView;
    }

}
