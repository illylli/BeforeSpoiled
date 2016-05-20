package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;

import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListDataSource;

/**
 * Created by Yuzhong on 2016/4/23.
 */
public class DeleteShoppingItemFromDatabase extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private long pos;
    ShoppingListDataSource mDataSource;

    public DeleteShoppingItemFromDatabase(){ }

    //Delete one entry from database.

    public DeleteShoppingItemFromDatabase(Context context, long pos){
        this.context = context;
        this.pos = pos;
    }

    @Override
    protected void onPreExecute() {
        // Getting reference to the TextView tv_counter of the layout activity_main
        mDataSource = new ShoppingListDataSource(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        mDataSource.open();
        mDataSource.deleteHistory(pos);
        mDataSource.close();
        return null;
    }
}
