package cs165.edu.dartmouth.cs.beforespoiled.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;

/**
 * Created by oubai on 5/25/16.
 */
public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.MyViewHolder> {

    private List<TemplateCover> templateCoverList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameView;
        public TextView desView;

        public MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.template_photo);
            nameView = (TextView) view.findViewById(R.id.template_name);
            desView = (TextView) view.findViewById(R.id.template_description);
        }
    }

    public TemplateAdapter(List<TemplateCover> templateCoverList) {
        this.templateCoverList = templateCoverList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_template_cardview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TemplateCover cover = templateCoverList.get(position);
        holder.imageView.setImageResource(cover.getPhotoId());
        holder.nameView.setText(cover.getTemplateName());
        holder.desView.setText(cover.getTemplateDes());
    }

    @Override
    public int getItemCount() {
        return templateCoverList.size();
    }
}
