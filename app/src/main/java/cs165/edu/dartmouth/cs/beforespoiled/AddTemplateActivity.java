package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;

import cs165.edu.dartmouth.cs.beforespoiled.database.SaveShoppingItemToDatabase;
import cs165.edu.dartmouth.cs.beforespoiled.database.SaveTemplateToDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.database.ShoppingListItem;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;

/**
 * Created by Yuzhong on 2016/5/27.
 */
public class AddTemplateActivity extends Activity {
    private EditText templateName;
    private EditText templateDes;
    private EditText text1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);

        templateName = (EditText) findViewById(R.id.template_name);
        templateDes = (EditText) findViewById(R.id.template_description);
        text1 = (EditText) findViewById(R.id.item1);

    }


    public void onTemplateSave(View view){
        String tName = templateName.getText().toString();
        String tDes = templateDes.getText().toString();
        int picId = 1;

        String text = text1.getText().toString();

        TemplateCover templateCover = new TemplateCover(tName, picId, tDes);
        SaveTemplateToDataSource task = new SaveTemplateToDataSource(getApplication().getApplicationContext(), templateCover);
        task.execute();

        try {
            templateCover = task.get();
            long id = templateCover.getId();
            ShoppingListItem shoppingListItem = new ShoppingListItem();
            shoppingListItem.setItemName(text);
            shoppingListItem.setListId(-id - 2);
            SaveShoppingItemToDatabase save = new SaveShoppingItemToDatabase(getApplicationContext(), shoppingListItem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    public void onTemplateCancel(View view){
        finish();
    }
}
