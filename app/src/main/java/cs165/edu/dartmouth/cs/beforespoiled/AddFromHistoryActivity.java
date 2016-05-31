package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
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

public class AddFromHistoryActivity extends Activity {

    public static final String ITEM_NAME = "item_name";
    public static final String BUY_DATE = "buy_date";

    private ExpandableListView expandableListView;
    private HistoryExpandableListAdapter adapter;
    private List<ShoppingLists> shoppinglists = new ArrayList<>();
    private Map<ShoppingLists, List<ShoppingListItem>> listsAndItems = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_from_history);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setSelection(0);


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
        expandableListView.setAdapter(adapter);

        int groupCount = expandableListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            expandableListView.expandGroup(i);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });




    }


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
