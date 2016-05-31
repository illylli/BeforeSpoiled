package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class DeleteShoppingItemFromDatabase extends AsyncTask<Void, Integer, Void> {

    ShoppingListItemDataSource mDataSource;
    private Context context;
    private long pos;
    private ArrayList<Long> poses = null;

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
