package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.UpdateShoppingItem;

/**
 * Created by Shicheng on 2016/5/28.
 */
public class TemplateItemAdapter extends ArrayAdapter<String> {
    private Context context;
    private List<String> list;

    public TemplateItemAdapter(Context context, List<String> list) {
        super(context, R.layout.activity_template_item, list);
        Log.d("View", "Hello + +");
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_template_item, null);
        }
        ((TextView)convertView.findViewById(R.id.textViewItem)).setText(list.get(position));
        return convertView;
    }

}
