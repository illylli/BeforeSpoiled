package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.vstechlab.easyfonts.EasyFonts;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateChild;

public class TemplateChildViewHolder extends ChildViewHolder {

    public TextView mIngredients;

    public TemplateChildViewHolder(View view) {
        super(view);
        mIngredients = (TextView) view.findViewById(R.id.template_ingredient);
    }

    public void bind(TemplateChild child) {
        mIngredients.setText(child.getIngredient());
    }
}
