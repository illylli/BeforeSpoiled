package cs165.edu.dartmouth.cs.beforespoiled.database;


import android.util.Log;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
    private List<String> items;

    public TemplateCover() {
        mChildrenList = new ArrayList<>();
        items = new ArrayList<>();
    }

    public TemplateCover(String name,  int id, String des) {
        this.templateName = name;
        this.templateDes = des;
        this.photoId = id;
        mChildrenList = new ArrayList<>();
        items = new ArrayList<>();
    }

    public TemplateCover(String name, String des, int id, List<TemplateChild> ingredients) {
        this.templateName = name;
        this.templateDes = des;
        this.photoId = id;
        mChildrenList = ingredients;
        items = new ArrayList<>();
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

    public List<String> getItems() {
        return items;
    }

    public void setItemsAll(List<String> items) {
        this.items = items;
    }

    public void setItems(String item) {
        this.items.add(item);
    }

    public String getItemsGson(){
        Gson gson = new Gson();
        String list = gson.toJson(items);
        return list;
    }

    public void setItemsFromGson(String list){
        Type type = new TypeToken<List<String>>() {}.getType();
        Gson gson = new Gson();
        items = gson.fromJson(list, type);

        Log.d("template", "hello : " + items + " * " + list);
        for(String item: items){
            TemplateChild child = new TemplateChild(item);
            mChildrenList.add(child);
        }
    }
}

