package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItemDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingLists;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListsDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.helper.DateHelper;
import cs165.edu.dartmouth.cs.beforespoiled.view.HistoryExpandableListAdapter;

public class AddFromHistoryActivity extends ExpandableListActivity {

    public static final String ITEM_NAME = "item_name";
    public static final String BUY_DATE = "buy_date";

    private HistoryExpandableListAdapter adapter;
    private List<ShoppingLists> shoppinglists = new ArrayList<>();
    private Map<ShoppingLists, List<ShoppingListItem>> listsAndItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_history);

        ShoppingListsDataSource listHelper = new ShoppingListsDataSource(this);
        listHelper.open();
        shoppinglists = listHelper.fetchRecentHistory(10);
        listHelper.close();
        ShoppingListItemDataSource itemHelper = new ShoppingListItemDataSource(this);
        itemHelper.open();
        for (ShoppingLists list : shoppinglists) {
            listsAndItems.put(list, itemHelper.getItemsByList(list.getId()));
        }
        itemHelper.close();
        adapter = new HistoryExpandableListAdapter(this, shoppinglists, listsAndItems);
        setListAdapter(adapter);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent intent = new Intent(this, ManualAddItemActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ITEM_NAME, listsAndItems.get(shoppinglists.get(groupPosition)).get(childPosition).getItemName());
        bundle.putString(BUY_DATE, DateHelper.formatDate(shoppinglists.get(groupPosition).getDate()));
        intent.putExtras(bundle);
        startActivity(intent);
        listsAndItems.get(shoppinglists.get(groupPosition)).remove(childPosition);
        adapter.notifyDataSetChanged();
        return true;
    }
}
