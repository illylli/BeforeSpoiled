package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.content.Context;
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

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.Card;

/**
 * Created by oubai on 5/22/16.
 */
public class CardArrayAdapter extends ArrayAdapter<Card> {
    private Context context;
    private List<Card> cardList;

    static class CardViewHolder {
        TextView itemName;
        TextView itemNumber;
        CheckBox ifBought;
    }

    public CardArrayAdapter(Context context, List<Card> cardList) {
        super(context, R.layout.fragment_list_item, cardList);
        Log.d("View", "Hello + +");
        this.context = context;
        this.cardList = cardList;
    }

    @Override
    public void add(Card card) {
        cardList.add(card);
        super.add(card);
    }

    @Override
    public int getCount() {
        return this.cardList.size();
    }

    @Override
    public Card getItem(int index) {
        return this.cardList.get(index);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.fragment_list_item, parent, false);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }
        Log.d("View", "Hello + +");
        viewHolder = new CardViewHolder();
        viewHolder.itemName = (TextView) row.findViewById(R.id.item_name);
//        viewHolder.itemNumber = (TextView) row.findViewById(R.id.item_number);
        viewHolder.ifBought = (CheckBox) row.findViewById(R.id.check_box);
        row.setTag(viewHolder);
        Card card = cardList.get(position);
        viewHolder.itemName.setText(card.getItemName());
//        viewHolder.itemNumber.setText(String.valueOf(card.getItemNumber()));
        viewHolder.ifBought.setChecked(card.isSelected());
        return row;
    }

    public Bitmap decodeToBitmap(byte[] decodedByte) {
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}