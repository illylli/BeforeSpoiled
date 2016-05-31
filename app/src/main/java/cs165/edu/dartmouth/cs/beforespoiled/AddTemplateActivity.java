package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.vstechlab.easyfonts.EasyFonts;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cs165.edu.dartmouth.cs.beforespoiled.database.SaveTemplateToDataBase;
import cs165.edu.dartmouth.cs.beforespoiled.database.TemplateCover;
import cs165.edu.dartmouth.cs.beforespoiled.database.UpdateTemplateItem;
import cs165.edu.dartmouth.cs.beforespoiled.view.TemplateItemAdapter;

/**
 * Created by Yuzhong on 2016/5/27.
 */
public class AddTemplateActivity extends Activity {
    public static final int RESULT_OK = -1;
    protected static final int RESULT_SPEECH = 1;
    private EditText templateName;
    private EditText templateDes;
    private EditText addItem;
    private EditText text2;
    private ListView templateItemList;
    private TemplateItemAdapter itemListAdpater;
    private List<String> itemList;
    private TextView textViewName;
    private TextView textViewDes;
    private TemplateCover templateCover;
    private boolean isEdit;
    private ImageButton speechButton;
    private BroadcastReceiver receiverSync = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            update(intent);
        }
    };

    public void update(Intent i) {
        int pos = i.getIntExtra("itemPosition", -1);
        if (pos != -1) {
            itemList.remove(pos);
            itemListAdpater.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_template);

        templateName = (EditText) findViewById(R.id.template_name);
        templateDes = (EditText) findViewById(R.id.template_description);
        templateItemList = (ListView) findViewById(R.id.template_listView);
        textViewName = (TextView) findViewById(R.id.textView2);
        textViewDes = (TextView) findViewById(R.id.textView3);
        textViewName.setTypeface(EasyFonts.caviarDreamsBold(getApplicationContext()));
        textViewDes.setTypeface(EasyFonts.caviarDreamsBold(getApplicationContext()));

        IntentFilter filter = new IntentFilter("DeleteItem");
        this.registerReceiver(receiverSync, filter);

        Intent i = getIntent();
        String temp = i.getStringExtra("template");
        if(temp != null) {
            Log.d("EditTemplate", temp + " string template");

            Type type = new TypeToken<TemplateCover>() {
            }.getType();
            Gson gson = new Gson();
            templateCover = gson.fromJson(temp, type);
        }

        itemList = new ArrayList<>();

        templateItemList.setLongClickable(true);
        templateItemList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.d("daniel", "long press");
                final SweetAlertDialog warningDialog = new SweetAlertDialog(AddTemplateActivity.this, SweetAlertDialog.WARNING_TYPE);
                warningDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                warningDialog.setTitleText("Delete this item?");
                warningDialog.setCancelText("No, cancel it");
                warningDialog.setConfirmText("Yes, delete it");
                warningDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        warningDialog.cancel();
                    }
                });
                warningDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        SweetAlertDialog successDialog = new SweetAlertDialog(AddTemplateActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                        successDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        successDialog.setTitle("Deleted!");
                        successDialog.setTitleText("This item is deleted!");
                        successDialog.show();
                        itemList.remove(position);
                        itemListAdpater.notifyDataSetChanged();
                        warningDialog.cancel();
                    }
                });
                warningDialog.show();
                return true;
            }
        });

        itemListAdpater = new TemplateItemAdapter(this, itemList);

        templateItemList.setAdapter(itemListAdpater);

        addItem = (EditText) findViewById(R.id.add_template);
        addItem.setCursorVisible(false);

        setData();

        addItem.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_NULL:
                        System.out.println("null for default_content: " + v.getText());
                        break;
                    case EditorInfo.IME_ACTION_SEND:
                        System.out.println("action send for email_content: " + v.getText());
                        break;
                    case EditorInfo.IME_ACTION_DONE:
                        String itemName = addItem.getText().toString();
                        if (!itemName.equals("")) {
                            addItem.setText("");
                            addItem.clearFocus();
                            addItem.setCursorVisible(false);
                            itemList.add(itemName);
                            Log.d("EditTemplate", "I am here");
                            Log.d("EditTemplate", "size" + itemList.size() + itemList);
                            itemListAdpater.notifyDataSetChanged();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(addItem.getWindowToken(), 0);
                        }
                        break;
                }
                return true;
            }
        });

        addItem.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    addItem.setText("");
                }
            }
        });

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem.setCursorVisible(true);
            }
        });

        speechButton = (ImageButton) findViewById(R.id.SpeakButton3);
        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    itemList.add(text.get(0).toString());
                    itemListAdpater.notifyDataSetChanged();
                }
                break;
            }
            default:
                break;

        }
    }


    public void setData(){
        Log.d("EditTemplate", templateCover + " this is template");

        if(templateCover != null){

            Log.d("EditTemplate", templateCover.getTemplateName() + " this is name!!! this is id " + templateCover.getId());
            templateName.setText(templateCover.getTemplateName());
            templateDes.setText(templateCover.getTemplateDes());

            List<String> l = templateCover.getItems();
            if(l == null) {
                l = new ArrayList<>();
            }
            for(String str: l) itemList.add(str);
//            itemListAdpater.clear();
//            itemListAdpater.notifyDataSetChanged();
            isEdit = true;
        }
    }


    public void onTemplateSave(View view){
        String tName = templateName.getText().toString();
        String tDes = templateDes.getText().toString();
        int picId = R.drawable.beverage;

        TemplateCover newTemplateCover = new TemplateCover(tName, picId, tDes);
        newTemplateCover.setItemsAll(itemList);

        if(!isEdit) {
            Log.d("EditTemplate", newTemplateCover + " add to database!!");
            SaveTemplateToDataBase task = new SaveTemplateToDataBase(getApplication().getApplicationContext(), newTemplateCover);
            task.execute();
        }else {
            newTemplateCover.setId(templateCover.getId());
            Log.d("EditTemplate", newTemplateCover + " update from database@@@ " + newTemplateCover.getId() + "****" + newTemplateCover.getItems());
            UpdateTemplateItem task = new UpdateTemplateItem(getApplication().getApplicationContext(), newTemplateCover);
            task.execute();
        }

        Intent i = new Intent("AddTemplate");
        sendBroadcast(i);
        Log.d("template", "Save template");
        finish();
    }

    public void onTemplateCancel(View view){
        finish();
    }
}
