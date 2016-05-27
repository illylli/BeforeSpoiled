package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.vstechlab.easyfonts.EasyFonts;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.Card;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.UpdateShoppingItem;

/**
 * Created by oubai on 5/22/16.
 */
public class CardArrayAdapter extends ArrayAdapter<ShoppingListItem> {
    private Context context;
    private List<ShoppingListItem> cardList;
    CardViewHolder viewHolder;

    static class CardViewHolder {
        TextView itemName;
        TextView itemNumber;
        CheckBox ifBought;
    }

    public CardArrayAdapter(Context context, List<ShoppingListItem> cardList) {
        super(context, R.layout.fragment_list_item, cardList);
        Log.d("View", "Hello + +");
        this.context = context;
        this.cardList = cardList;
    }

    @Override
    public void add(ShoppingListItem card) {
        cardList.add(card);
        super.add(card);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public ShoppingListItem getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.fragment_list_item, parent, false);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        viewHolder = new CardViewHolder();
        viewHolder.itemName = (TextView) row.findViewById(R.id.item_name);
        viewHolder.itemName.setTypeface(EasyFonts.caviarDreamsBold(getContext()));
//        viewHolder.itemNumber = (TextView) row.findViewById(R.id.item_number);
        viewHolder.ifBought = (CheckBox) row.findViewById(R.id.check_box);
        row.setTag(viewHolder);
        final ShoppingListItem card = cardList.get(position);
        viewHolder.itemName.setText(card.getItemName());
//        viewHolder.itemNumber.setText(String.valueOf(card.getItemNumber()));
        viewHolder.ifBought.setChecked(card.isSelected());

        if(card.isSelected()) viewHolder.itemName.setPaintFlags(viewHolder.itemName.getPaintFlags() | (Paint.STRIKE_THRU_TEXT_FLAG));
        else viewHolder.itemName.setPaintFlags(viewHolder.itemName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));

        viewHolder.ifBought.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UpdateShoppingItem task;
                Intent intent = new Intent("Check");

                if(isChecked) {
                    card.setSelected(true);
                    task = new UpdateShoppingItem(getContext(), card);
                    task.execute();
                } else {
                    card.setSelected(false);
                    task = new UpdateShoppingItem(getContext(), card);
                    task.execute();
                }
                context.sendBroadcast(intent);
            }
        });
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}