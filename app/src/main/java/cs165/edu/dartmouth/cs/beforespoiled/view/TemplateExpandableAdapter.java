package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateChild;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateChildViewHolder;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateParentViewHolder;

/**
 * Created by oubai on 5/27/16.
 */
public class TemplateExpandableAdapter extends ExpandableRecyclerAdapter<TemplateParentViewHolder, TemplateChildViewHolder> {

    private LayoutInflater mInflater;

    public TemplateExpandableAdapter(Context context, @NonNull List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public TemplateParentViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View view = mInflater.inflate(R.layout.activity_template_parentview, parentViewGroup, false);
        return new TemplateParentViewHolder(mInflater.getContext(), view);

    }

    @Override
    public TemplateChildViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        Log.d("fuck", "getchild");
        View view = mInflater.inflate(R.layout.activity_template_childview, childViewGroup, false);
        return new TemplateChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(TemplateParentViewHolder parentViewHolder, int i, ParentListItem parentListItem) {
        Log.d("fuck", "parentviewholder");
        TemplateCover cover = (TemplateCover) parentListItem;
        parentViewHolder.setCoverPosition(i);
        parentViewHolder.bind(cover);
    }

    @Override
    public void onBindChildViewHolder(TemplateChildViewHolder childViewHolder, int i, Object childListItem) {
        Log.d("fuck", "childviewholder");
        TemplateChild child = (TemplateChild) childListItem;
        childViewHolder.bind(child);
    }


}
