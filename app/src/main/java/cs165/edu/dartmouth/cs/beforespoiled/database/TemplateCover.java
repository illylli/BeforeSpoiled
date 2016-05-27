package cs165.edu.dartmouth.cs.beforespoiled.database;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by oubai on 5/25/16.
 */
public class TemplateCover implements ParentListItem {
    private long id;
    private String templateName;
    private String templateDes;
    private int photoId;
    private List<TemplateChild> mChildrenList;

    public TemplateCover() {}

    public TemplateCover(String name,  int id, String des) {
        this.templateName = name;
        this.templateDes = des;
        this.photoId = id;
    }

    public TemplateCover(String name, String des, int id, List ingredients) {
        this.templateName = name;
        this.templateDes = des;
        this.photoId = id;
        mChildrenList = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String name) {
        this.templateName = name;
    }

    public String getTemplateDes() {
        return templateDes;
    }

    public void setTemplateDes(String des) {
        this.templateDes = des;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int id) {
        this.photoId = id;
    }

    @Override
    public List<?> getChildItemList() {
        return mChildrenList;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}

//private List<TemplateCover> templates;
//
//private void initializeTemplate() {
//    templates = new ArrayList<>();
//    templates.add(new TemplateCover("meal 1", "description 1", R.drawable.beverage));
//    templates.add(new TemplateCover("meal 2", "description 2", R.drawable.beverage));
//    templates.add(new TemplateCover("meal 3", "description 3", R.drawable.beverage));
//}
