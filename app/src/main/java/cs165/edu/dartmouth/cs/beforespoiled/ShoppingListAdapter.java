package cs165.edu.dartmouth.cs.beforespoiled;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingListItem> {
    private Context context;
    private ArrayList<ShoppingListItem> shoppingListItems;

    public ShoppingListAdapter(Context context, ArrayList<ShoppingListItem> shoppingListItems) {
        super(context, R.layout.shopping_list_item, shoppingListItems);
        this.context = context;
        this.shoppingListItems = shoppingListItems;
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
        View twoLineListItem;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            twoLineListItem = inflater.inflate(
                    R.layout.shopping_list_item, null);
        } else {
            twoLineListItem = convertView;
        }

        TextView text1 = (TextView) twoLineListItem.findViewById(R.id.item_name);
        TextView text2 = (TextView) twoLineListItem.findViewById(R.id.item_number);

        text1.setTextColor(Color.GRAY);
        text2.setTextColor(Color.GRAY);
        text1.setText(getFirstRow(shoppingListItems.get(position)));
        text2.setText(getSecondRow(shoppingListItems.get(position)));

        return twoLineListItem;
    }
    //Show the first row in history entry list.
    public String getFirstRow(ShoppingListItem shoppingListItem){
        return "Row1";
    }
    //Show the second row in history entry list.
    public String getSecondRow(ShoppingListItem shoppingListItem){
        return "Row2";
    }

}
