package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReadShoppingListFromDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReadTemplateFromDataBase;
import cs165.edu.dartmouth.cs.beforespoiled.database.SaveShoppingItemToDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateChild;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateChildViewHolder;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateExpandableAdapter;


/**
 * Created by oubai on 5/25/16.
 */
public class TemplateActivity extends Activity implements LoaderManager.LoaderCallbacks<List<TemplateCover>>{

    private List<TemplateCover> templateCoverList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TemplateExpandableAdapter mAdapter;
    private RecyclerView recyclerView;
    Intent edit = new Intent(this, AddTemplateActivity.class);

    private BroadcastReceiver receiverSync = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            mAdapter.notifyDataSetChanged();
            update();
        }
    };

    private BroadcastReceiver receiverAddToList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra("Position", -1);
            Log.d("template", "addtolist" + pos);

            if(pos != -1) saveToShoppingList(pos);
        }
    };

    private BroadcastReceiver receiverEdit = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivity(edit);
        }
    };


    public void update(){
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_recyclerview);

        IntentFilter filter = new IntentFilter("AddTemplate");
        this.registerReceiver(receiverSync, filter);

        IntentFilter filter2 = new IntentFilter("AddToList");
        this.registerReceiver(receiverAddToList, filter2);

        TemplateChild beef = new TemplateChild("beef");
        TemplateChild cheese = new TemplateChild("cheese");
        TemplateChild salsa = new TemplateChild("salsa");
        TemplateChild tortilla = new TemplateChild("tortilla");
        TemplateCover cover = new TemplateCover("meal 1", "description 1", R.drawable.beverage, Arrays.asList(beef, cheese, salsa, tortilla));
        templateCoverList.add(cover);
        cover = new TemplateCover("meal 2", "description 2", R.drawable.beverage, Arrays.asList(beef, cheese, salsa, tortilla));
        templateCoverList.add(cover);
        cover = new TemplateCover("meal 3", "description 3", R.drawable.beverage, Arrays.asList(beef, cheese, salsa, tortilla));
        templateCoverList.add(cover);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new TemplateExpandableAdapter(this, templateCoverList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        update();
    }

    //!!!!!!!!!!!!!!!save template!!!!!!!!!!!!
    public void saveToShoppingList(int position){
        TemplateCover cover = templateCoverList.get(position);
        long id = cover.getId();

        Log.d("template", "addtoshoppinglist !!!");
        List<String> items = cover.getItems();

        for(String item: items){
            ShoppingListItem shoppingListItem = new ShoppingListItem();
            shoppingListItem.setItemName(item);
            SaveShoppingItemToDatabase task = new SaveShoppingItemToDatabase(this, shoppingListItem);
            task.execute();
            try {
                Thread.sleep(2000);
                Intent intent = new Intent("Check");
                sendBroadcast(intent);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void onAddTemplateClick(View view){
        Intent intent = new Intent(this, AddTemplateActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public Loader<List<TemplateCover>> onCreateLoader(int id, Bundle args) {
        return new ReadTemplateFromDataBase(getApplication().getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<TemplateCover>> loader, List<TemplateCover> data) {
        templateCoverList.clear();
        Log.d("template", data.size() + " ");
        if(data != null)
            templateCoverList.addAll(data);
        mAdapter = new TemplateExpandableAdapter(this, templateCoverList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLoaderReset(Loader<List<TemplateCover>> loader) {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiverSync);
        unregisterReceiver(receiverAddToList);
    }
}
