package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class SaveShoppingItemToDatabase extends AsyncTask<Void, Integer, ShoppingListItem> {
    private ShoppingListItemDataSource mDataSource;
    private ShoppingListItem shoppingListItem;
    private ShoppingListItem newShoppingListItem;
    private Context context;

    public SaveShoppingItemToDatabase(Context context, ShoppingListItem shoppingListItem){
        this.context = context;
        this.shoppingListItem = shoppingListItem;
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

    // A callback method executed on UI thread, invoked by the publishProgress()
    // from doInBackground() method

    // Overrider this handler to post interim updates to the UI thread. This handler receives the set of parameters
    // passed in publishProgress from within doInbackground.
    @Override
    protected void onProgressUpdate(Integer... values) {
        // Getting reference to the TextView tv_counter of the layout activity_main

    }

    // A callback method executed on UI thread, invoked after the completion of the task

    // When doInbackground has completed, the return value from that method is passed into this event
    // handler.
}
