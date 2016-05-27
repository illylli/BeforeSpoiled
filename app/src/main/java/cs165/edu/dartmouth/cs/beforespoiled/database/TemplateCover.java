package cs165.edu.dartmouth.cs.beforespoiled.database;

import java.util.ArrayList;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.R;

/**
 * Created by oubai on 5/25/16.
 */
public class TemplateCover {
    private String templateName;
    private String templateDes;
    private int photoId;

    public TemplateCover(String name, String des, int id) {
        this.templateName = name;
        this.templateDes = des;
        this.photoId = id;
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
}

//private List<TemplateCover> templates;
//
//private void initializeTemplate() {
//    templates = new ArrayList<>();
//    templates.add(new TemplateCover("meal 1", "description 1", R.drawable.beverage));
//    templates.add(new TemplateCover("meal 2", "description 2", R.drawable.beverage));
//    templates.add(new TemplateCover("meal 3", "description 3", R.drawable.beverage));
//}
