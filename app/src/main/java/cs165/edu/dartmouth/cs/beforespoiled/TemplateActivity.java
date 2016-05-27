package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateAdapter;

/**
 * Created by oubai on 5/25/16.
 */
public class TemplateActivity extends Activity {
    private List<TemplateCover> templateCoverList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_recyclerview);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mAdapter = new TemplateAdapter(templateCoverList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        prepareTempateData();
    }

    private void prepareTempateData() {
        TemplateCover cover = new TemplateCover("meal 1", "description 1", R.drawable.beverage);
        templateCoverList.add(cover);
        cover = new TemplateCover("meal 2", "description 2", R.drawable.beverage);
        templateCoverList.add(cover);
        cover = new TemplateCover("meal 3", "description 3", R.drawable.beverage);
        templateCoverList.add(cover);

        mAdapter.notifyDataSetChanged();
    }
}
