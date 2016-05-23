package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Yuzhong on 2016/5/19.
 */
public class ReadShoppingListFromDatabase extends AsyncTaskLoader<List<Card>> {

    private ShoppingListDataSource mDataSource;
    private Context context;

    private static final String TAG = "AsyncTask";

    public ReadShoppingListFromDatabase(Context context){
        super(context);
        this.context = context;
        mDataSource = new ShoppingListDataSource(context);
    }

    @Override
    public List<Card> loadInBackground() {
        mDataSource.open();
        // read data from database
//        List<ShoppingListItem> list = mDataSource.getAllItems();
        List<Card> list = mDataSource.getAllItems();

        Log.d("Database", "Read All:" + list.size());

        mDataSource.close();
        return list;
    }

}
