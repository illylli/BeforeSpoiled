package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.SaveShoppingItemToDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;

/**
 * Created by oubai on 5/27/16.
 */
public class TemplateParentViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATED_POSITION = 180f;

    public ImageView imageView;
    public TextView nameView;
    public TextView desView;
    public ImageView arrayExpand;
    public Button toShoppingList;
    public Button edit;
    public TemplateCover templateCover;
    public Context context;
    public int position;

    public TemplateParentViewHolder(View view) {
        super(view);
        imageView = (ImageView) view.findViewById(R.id.template_photo);
        nameView = (TextView) view.findViewById(R.id.template_name);
        desView = (TextView) view.findViewById(R.id.template_description);
        arrayExpand = (ImageView) view.findViewById(R.id.template_arrow);
        toShoppingList = (Button) view.findViewById(R.id.to_shopping_list);
    }

    public TemplateParentViewHolder(Context con, final View view) {
        super(view);
        imageView = (ImageView) view.findViewById(R.id.template_photo);
        nameView = (TextView) view.findViewById(R.id.template_name);
        desView = (TextView) view.findViewById(R.id.template_description);
        arrayExpand = (ImageView) view.findViewById(R.id.template_arrow);
        toShoppingList = (Button) view.findViewById(R.id.to_shopping_list);
        edit = (Button) view.findViewById(R.id.to_shopping_list);
        context = con;

        toShoppingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("AddToList");
                intent.putExtra("Position", position);
                context.sendBroadcast(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("Edit");
                intent.putExtra("Position", position);
                context.sendBroadcast(intent);
            }
        });
    }


    public void bind(TemplateCover cover) {
        imageView.setImageResource(cover.getPhotoId());
        nameView.setText(cover.getTemplateName());;
        desView.setText(cover.getTemplateDes());
    }


    public int getCoverPosition() {
        return position;
    }

    public void setCoverPosition(int position) {
        this.position = position;
    }

    @Override
    public void setExpanded(boolean expanded) {
        super.setExpanded(expanded);
        if (expanded) {
            arrayExpand.setRotation(ROTATED_POSITION);
        } else {
            arrayExpand.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            RotateAnimation rotateAnimation;
            if (expanded) { // rotate clockwise
                rotateAnimation = new RotateAnimation(ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            } else { // rotate counterclockwise
                rotateAnimation = new RotateAnimation(-1 * ROTATED_POSITION,
                        INITIAL_POSITION,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                        RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            }

            rotateAnimation.setDuration(200);
            rotateAnimation.setFillAfter(true);
            arrayExpand.startAnimation(rotateAnimation);
        }
    }
}
