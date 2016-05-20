package cs165.edu.dartmouth.cs.beforespoiled;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.adapters.*;
import com.daimajia.swipe.adapters.ArraySwipeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class ShoppingListAdapter extends ArraySwipeAdapter {
    private Context context;
    private ArrayList<ShoppingListItem> shoppingListItems;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> shoppingListItems) {
        super(context, R.layout.shopping_list_item, shoppingListItems);
        this.context = context;
//        this.shoppingListItems = shoppingListItems;
        this.shoppingListItems = new ArrayList<ShoppingListItem>();

        this.shoppingListItems.addAll(shoppingListItems);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return 0;
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

            holder = new ViewHolder();
            holder.itemName = (TextView) convertView.findViewById(R.id.item_name);
            holder.ifBought = (CheckBox) convertView.findViewById(R.id.check_box);
            convertView.setTag(holder);

            holder.itemName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    ShoppingListItem listItem = (ShoppingListItem) checkBox.getTag();
                    Toast.makeText(getContext(), "Clicked on Checkbox" + checkBox.isChecked(), Toast.LENGTH_LONG).show();
                    listItem.setSelected(checkBox.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ShoppingListItem listItem = shoppingListItems.get(position);
        holder.itemName.setText(listItem.getItemName());
        holder.ifBought.setChecked(listItem.isSelected());

        return convertView;
//        View twoLineListItem;
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            twoLineListItem = inflater.inflate(
//                    R.layout.shopping_list_item, null);
//        } else {
//            twoLineListItem = convertView;
//        }
//
//        TextView text1 = (TextView) twoLineListItem.findViewById(R.id.item_name);
//        TextView text2 = (TextView) twoLineListItem.findViewById(R.id.item_number);
//
//        text1.setTextColor(Color.GRAY);
//        text2.setTextColor(Color.GRAY);
//        text1.setText(getFirstRow(shoppingListItems.get(position)));
//        text2.setText(getSecondRow(shoppingListItems.get(position)));
//
//        return twoLineListItem;
    }

    //Show the first row in history entry list.
//    public String getFirstRow(ShoppingListItem shoppingListItem){
//        return "Row1";
//    }
    //Show the second row in history entry list.
//    public String getSecondRow(ShoppingListItem shoppingListItem){
//        return "Row2";
//    }

}
