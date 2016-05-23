package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * Created by Yuzhong on 2016/4/19.
 */
public class SaveShoppingItemToDatabase extends AsyncTask<Void, Integer, Void> {
    private ShoppingListDataSource mDataSource;
//    private ShoppingListItem shoppingListItem;
//    private ShoppingListItem newShoppingListItem;
    private Card card;
    private Card newCard;
    private Context context;

    public SaveShoppingItemToDatabase(Context context, Card card){
        this.context = context;
//        this.shoppingListItem = shoppingListItem;
        this.card = card;
    }
    // A callback method executed on UI thread on starting the task
    @Override
    protected void onPreExecute() {
        // Getting reference to the TextView tv_counter of the layout activity_main
        mDataSource = new ShoppingListDataSource(context);
    }

    // A callback method executed on non UI thread, invoked after
    // onPreExecute method if exists

    // Takes a set of parameters of the type defined in your class implementation. This method will be
    // executed on the background thread, so it must not attempt to interact with UI objects.
    @Override
    protected Void doInBackground(Void... params) {
        mDataSource.open();
//        Log.d("Database", "Save data:" + shoppingListItem);
//        newShoppingListItem = mDataSource.createHistory(shoppingListItem);
        newCard = mDataSource.createHistory(card);

        mDataSource.close();
        return null;
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
    @Override
    protected void onPostExecute(Void result) {
        // Getting reference to the TextView tv_counter of the layout activity_main
        Toast toast = Toast.makeText(context, "Entry #" + newCard.getId() + "Saved", Toast.LENGTH_SHORT);
        toast.show();
    }
}
