package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.UpdateShoppingItem;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {
    private Context context;
    private List<ShoppingListItem> shoppingListItems;

    public ShoppingListAdapter(Context context, List<ShoppingListItem> shoppingListItems) {
        super(context, R.layout.shopping_list_item, shoppingListItems);
        this.context = context;
        this.shoppingListItems = shoppingListItems;
//        this.shoppingListItems = new ArrayList<ShoppingListItem>();
//        this.shoppingListItems.addAll(shoppingListItems);
    }

    private class ViewHolder {
        TextView itemName;
        CheckBox ifBought;
    }

    @Override
    public int getCount() {
        return shoppingListItems.size();
    }

    @Override
    public ShoppingListItem getItem(int position) {
        return shoppingListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.shopping_list_item, null);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder = new ViewHolder();
        holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
        holder.ifBought = (CheckBox) convertView.findViewById(R.id.check_box);
        final ShoppingListItem listItem = shoppingListItems.get(position);

        holder.ifBought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                UpdateShoppingItem task;
                if(isChecked){
                    listItem.setSelected(true);
                    task = new UpdateShoppingItem(getContext(), listItem);
                    task.execute();
                }else{
                    listItem.setSelected(false);
                    task = new UpdateShoppingItem(getContext(), listItem);
                    task.execute();
                }
            }
        });


        convertView.setTag(holder);

        holder.itemName.setTextColor(Color.GRAY);

        holder.itemName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                ShoppingListItem listItem = (ShoppingListItem) checkBox.getTag();
                Toast.makeText(getContext(), "Clicked on Checkbox" + checkBox.isChecked(), Toast.LENGTH_LONG).show();
                listItem.setSelected(checkBox.isChecked());
            }
        });


        Log.d("Database", "@@@"+listItem.getItemName());
        holder.itemName.setText(listItem.getItemName());
        holder.ifBought.setChecked(listItem.isSelected());

        return convertView;
    }


}
