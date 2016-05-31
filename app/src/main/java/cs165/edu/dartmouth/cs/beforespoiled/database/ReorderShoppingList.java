package cs165.edu.dartmouth.cs.beforespoiled.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.UIReloader;

/**
 * Created by Fanzy on 5/30/16.
 */
public class ReorderShoppingList extends AsyncTask<Void, Integer, Void> {

    private Context context;
    private UIReloader reloader;

    public ReorderShoppingList(Context context, UIReloader reloader){
        this.context = context;
        this.reloader = reloader;
    }

    @Override
    protected Void doInBackground(Void... args) {
        ShoppingListItemDataSource dataSource = new ShoppingListItemDataSource(context);
        dataSource.open();
        // read data from database
        List<ShoppingListItem> list = dataSource.getItemsBySelected();
        dataSource.deleteCurrentList();
        for(ShoppingListItem item : list){
            dataSource.createHistory(item);
        }
        dataSource.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        reloader.reloadData();
    }
}
