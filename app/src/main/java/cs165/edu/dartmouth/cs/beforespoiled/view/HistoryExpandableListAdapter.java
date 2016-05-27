package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingLists;

/**
 * Created by Fanzy on 5/24/16.
 */
public class HistoryExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ShoppingLists> shoppinglists; // header titles
    // child data in format of header title, child title
    private Map<ShoppingLists, List<ShoppingListItem>> listsAndItems;

    public HistoryExpandableListAdapter(Context context, List<ShoppingLists> listDataHeader, Map<ShoppingLists, List<ShoppingListItem>> listChildData) {
        mContext = context;
        shoppinglists = listDataHeader;
        listsAndItems = listChildData;
    }

    @Override
    public int getGroupCount() {
        return shoppinglists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listsAndItems.get(shoppinglists.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return shoppinglists.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listsAndItems.get(shoppinglists.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return shoppinglists.get(i).getId();
    }

    @Override
    public long getChildId(int i, int i1) {
        return listsAndItems.get(shoppinglists.get(i)).get(i1).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ShoppingLists lists = (ShoppingLists) getGroup(i);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
        }

        TextView lblListHeader = (TextView) view.findViewById(android.R.id.text1);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(lists.getDate().toString());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final ShoppingListItem listItem = (ShoppingListItem) getChild(i, i1);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
        }

        TextView txtListChild = (TextView) view.findViewById(android.R.id.text1);
        txtListChild.setText(listItem.getItemName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
