package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Yuzhong on 2016/4/23.
 */
public class DeleteShoppingItemFromDatabase extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private long pos;
    private ArrayList<Long> poses = null;
    ShoppingListItemDataSource mDataSource;

    public DeleteShoppingItemFromDatabase(){ }

    //Delete one entry from database.

    public DeleteShoppingItemFromDatabase(Context context, long pos){
        this.context = context;
        this.pos = pos;
    }

    public DeleteShoppingItemFromDatabase(Context context, ArrayList<Long> pos){
        this.context = context;
        this.poses = pos;
    }

    @Override
    protected void onPreExecute() {
        // Getting reference to the TextView tv_counter of the layout activity_main
        mDataSource = new ShoppingListItemDataSource(context);
    }

    @Override
    protected Void doInBackground(Void... params) {
        mDataSource.open();
        if(poses != null){
            for(long p : poses){
                mDataSource.deleteHistory(p);
            }
        } else {
            mDataSource.deleteHistory(pos);
        }
        mDataSource.close();
        return null;
    }
}
