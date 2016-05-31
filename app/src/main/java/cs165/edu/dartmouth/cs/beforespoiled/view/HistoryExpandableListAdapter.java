package cs165.edu.dartmouth.cs.beforespoiled.view;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;
import java.util.Map;

import cs165.edu.dartmouth.cs.beforespoiled.ManualAddItemActivity;
import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingLists;
import cs165.edu.dartmouth.cs.beforespoiled.helper.DateHelper;

public class HistoryExpandableListAdapter extends BaseExpandableListAdapter {
    // child data in format of header title, child title
    public static final String ITEM_NAME = "item_name";
    public static final String BUY_DATE = "buy_date";
    private Context mContext;
    private LayoutInflater inflater = null;
    private List<ShoppingLists> shoppinglists; // header titles
    private Map<ShoppingLists, List<ShoppingListItem>> listsAndItems;

    public HistoryExpandableListAdapter(Context context, List<ShoppingLists> listDataHeader, Map<ShoppingLists, List<ShoppingListItem>> listChildData) {
        mContext = context;
        shoppinglists = listDataHeader;
        listsAndItems = listChildData;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getGroupCount() {
        return shoppinglists.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listsAndItems.get(shoppinglists.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return shoppinglists.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        Log.d("lly", "" + listsAndItems.get(shoppinglists.get(i)).get(i1).getClass());
        return listsAndItems.get(shoppinglists.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return shoppinglists.get(i).getId();
    }

    @Override
    public long getChildId(int i, int i1) {
        return listsAndItems.get(shoppinglists.get(i)).get(i1).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {


        GroupViewHolder holder = new GroupViewHolder();
        ShoppingLists lists = (ShoppingLists) getGroup(i);
        if (view == null) {

            view = inflater.inflate(R.layout.group_status_item, null);
        }

        //else {
        holder.groupName = (TextView) view.findViewById(R.id.one_status_name);
        // TextView lblListHeader = (TextView) view.findViewById(android.R.id.text1);

        if (lists.getDate() == null) {
            holder.groupName.setText("");
        } else {
            holder.groupName.setText(lists.getDate().toString());
            holder.groupName.setTypeface(EasyFonts.caviarDreamsBold(mContext));

        }
        holder.groupName.setTypeface(null, Typeface.BOLD);
//        }


        //lblListHeader.setTextSize(15);

        return view;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View view, ViewGroup paren) {

        ChildViewHolder viewHolder = null;
        final ShoppingListItem listItem = (ShoppingListItem) getChild(groupPosition, childPosition);

        if (view != null) {
//            LayoutInflater infalInflater = (LayoutInflater) this.mContext
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = infalInflater.inflate(android.R.layout.simple_expandable_list_item_2, null);
            viewHolder = (ChildViewHolder) view.getTag();
        } else {

            viewHolder = new ChildViewHolder();
            view = inflater.inflate(R.layout.child_status_item, null);

            viewHolder.childTitle = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.swipeLayout = (SwipeLayout) view.findViewById(R.id.sample);
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewWithTag("Bottom2"));
            viewHolder.iv_star = (ImageView) view.findViewById(R.id.star);

            view.setTag(viewHolder);

        }


        if (listItem != null) {
            viewHolder.childTitle.setText(listItem.getItemName());
            viewHolder.childTitle.setTextSize(17);
            viewHolder.childTitle.setTypeface(EasyFonts.caviarDreamsBold(mContext));
            viewHolder.iv_star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast("click star");
                    //do something
                    Intent intent = new Intent(mContext, ManualAddItemActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(ITEM_NAME, listsAndItems.get(shoppinglists.get(groupPosition)).get(childPosition).getItemName());
                    bundle.putString(BUY_DATE, DateHelper.formatDate(shoppinglists.get(groupPosition).getDate()));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                    listsAndItems.get(shoppinglists.get(groupPosition)).remove(childPosition);
                    notifyDataSetChanged();
                }
            });

            final ChildViewHolder finalViewHolder = viewHolder;
            view.findViewById(R.id.item_surface).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //do something
                    finalViewHolder.swipeLayout.open(false);


                }
            });


        }

//        TextView txtListChild = (TextView) view.findViewById(android.R.id.text1);
//        txtListChild.setTextSize(10);
        // txtListChild.setText(listItem.getItemName());
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

    }

    static class ChildViewHolder {
        public SwipeLayout swipeLayout;
        public ImageView iv_star;
        public TextView childTitle;
    }

    static class GroupViewHolder {
        TextView groupName;
    }



}
