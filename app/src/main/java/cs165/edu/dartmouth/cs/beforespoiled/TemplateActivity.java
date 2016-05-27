package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateChild;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateExpandableAdapter;


/**
 * Created by oubai on 5/25/16.
 */
public class TemplateActivity extends Activity {

    private List<TemplateCover> templateCoverList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TemplateExpandableAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_recyclerview);

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new TemplateExpandableAdapter(this, templateCoverList);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        recyclerView.setAdapter(mAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new TemplateExpandableAdapter(this, templateCoverList);
//        mRecyclerView.setAdapter(mAdapter);

//        setContentView(R.layout.activity_template_recyclerview);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new TemplateExpandableAdapter(this, prepareTempateData());
////        mAdapter.onRestoreInstanceState(savedInstanceState);
//        mRecyclerView.setAdapter(mAdapter);
    }

//    private List<ParentListItem> prepareTempateData() {
//        TemplateChild beef = new TemplateChild("beef");
//        TemplateChild cheese = new TemplateChild("cheese");
//        TemplateChild salsa = new TemplateChild("salsa");
//        TemplateChild tortilla = new TemplateChild("tortilla");
//        TemplateCover cover = new TemplateCover("meal 1", "description 1", R.drawable.beverage, Arrays.asList(beef, cheese, salsa, tortilla));
//        templateCoverList.add(cover);
//        cover = new TemplateCover("meal 2", "description 2", R.drawable.beverage, Arrays.asList(beef, cheese, salsa, tortilla));
//        templateCoverList.add(cover);
//        cover = new TemplateCover("meal 3", "description 3", R.drawable.beverage, Arrays.asList(beef, cheese, salsa, tortilla));
//        templateCoverList.add(cover);
//
//        return templateCoverList;
//    }

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
}
