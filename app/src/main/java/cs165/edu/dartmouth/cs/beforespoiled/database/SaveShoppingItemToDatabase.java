package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import cs165.edu.dartmouth.cs.beforespoiled.UIReloader;

public class SaveShoppingItemToDatabase extends AsyncTask<Void, Integer, ShoppingListItem> {
    private ShoppingListItemDataSource mDataSource;
    private ShoppingListItem shoppingListItem;
    private ShoppingListItem newShoppingListItem;
    private Context context;
    private UIReloader reloader;

    public SaveShoppingItemToDatabase(Context context, ShoppingListItem shoppingListItem, UIReloader reloader) {
        this.context = context;
        this.shoppingListItem = shoppingListItem;
        this.reloader = reloader;
    }
    // A callback method executed on UI thread on starting the task
    @Override
    protected void onPreExecute() {
        // Getting reference to the TextView tv_counter of the layout activity_main
        mDataSource = new ShoppingListItemDataSource(context);
    }

    // A callback method executed on non UI thread, invoked after
    // onPreExecute method if exists

    // Takes a set of parameters of the type defined in your class implementation. This method will be
    // executed on the background thread, so it must not attempt to interact with UI objects.
    @Override
    protected ShoppingListItem doInBackground(Void... params) {
        mDataSource.open();
        Log.d("Database", "Save data:" + shoppingListItem);
        shoppingListItem.setSelected(false);
        newShoppingListItem = mDataSource.createHistory(shoppingListItem);

        mDataSource.close();
        return newShoppingListItem;
    }

    @Override
    protected void onPostExecute(ShoppingListItem shoppingListItem) {
        super.onPostExecute(shoppingListItem);
        if (reloader != null) {
            reloader.reloadData();
        }
    }
}
