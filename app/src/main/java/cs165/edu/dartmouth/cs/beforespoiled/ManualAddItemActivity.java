package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cs165.edu.dartmouth.cs.beforespoiled.database.Label;
import cs165.edu.dartmouth.cs.beforespoiled.database.LabelDataSource;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntry;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryAsyncTask;
import cs165.edu.dartmouth.cs.beforespoiled.helper.DateHelper;

public class ManualAddItemActivity extends Activity {

    public static final int TAKE_PHOTO_REQUEST_CODE = 0;
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private TextView mDisplayDateTime;
    private Calendar mBaseDateAndTime = Calendar.getInstance();
    private Calendar mDateAndTime = Calendar.getInstance();
    private EditText itemName;
    private ImageButton cameraButton;
    private Spinner categorySpinner;
    private Uri mImageCaptureUri;
    private String filePath;

    private int[] imageResource = {R.drawable.vege, R.drawable.fruit, R.drawable.bread, R.drawable.milk, R.drawable.spices
            ,R.drawable.frozen, R.drawable.grain, R.drawable.snack, R.drawable.beverage, R.drawable.fish};
    private ArrayAdapter adapter = null;
    private List<Label> labels = new ArrayList<>();

    private boolean photoExist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_add_item);

        LabelDataSource dataSource = new LabelDataSource(this);
        dataSource.open();
        labels = dataSource.fetchEntries();
        dataSource.close();

        //User type item name
        itemName = (EditText) findViewById(R.id.editTextName);
        itemName.clearFocus();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemName.setText(bundle.getString(AddFromHistoryActivity.ITEM_NAME));
            mBaseDateAndTime = DateHelper.dataToCalendar(bundle.getString(AddFromHistoryActivity.BUY_DATE));
        }

        //User change expire date by clicking image button.
        mDisplayDateTime = (TextView) findViewById(R.id.DateDisplayView);
        mDateAndTime.setTime(mBaseDateAndTime.getTime());
        mDateAndTime.add(Calendar.DATE, labels.get(0).getStoragePeriod());
        updateDateAndTimeDisplay();

        cameraButton = (ImageButton) findViewById(R.id.ShowCameraButton);
        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_"
                        + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        mImageCaptureUri);

                startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);

            }
        });
        categorySpinner = (Spinner)findViewById(R.id.spinnerCategory);
        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, labels.toArray());
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!photoExist) {
                    cameraButton.setImageResource(imageResource[position]);
                    cameraButton.setScaleType(ImageView.ScaleType.FIT_XY);
                    mDateAndTime.setTime(mBaseDateAndTime.getTime());
                    mBaseDateAndTime.add(Calendar.DATE, labels.get(position).getStoragePeriod());
                    updateDateAndTimeDisplay();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                cameraButton.setImageResource(R.drawable.fish);
            }
        });
    }
    //When click the best before image button (pencil)
    public void onDateClicked(View v) {

        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDateAndTime.set(Calendar.YEAR, year);
                mDateAndTime.set(Calendar.MONTH, monthOfYear);
                mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateAndTimeDisplay();
            }
        };

        new DatePickerDialog(ManualAddItemActivity.this, mDateListener,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void updateDateAndTimeDisplay() {
        mDisplayDateTime.setText(DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE
                        | DateUtils.FORMAT_SHOW_YEAR));
    }

    public void onCancelClicked(View v) {
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the image capture uri before the activity goes into background
        outState.putParcelable(URI_INSTANCE_STATE_KEY, mImageCaptureUri);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case TAKE_PHOTO_REQUEST_CODE:
                beginCrop(mImageCaptureUri);
                break;
            case Crop.REQUEST_CROP: //We changed the RequestCode to the one being used by the library.
                // Update image view after image crop
                handleCrop(resultCode, data);
                // Delete temporary image taken by camera after crop.
                File f = new File(mImageCaptureUri.getPath());
                if (f.exists())
                    f.delete();
                break;

        }
    }

    private void beginCrop(Uri source) {
        filePath = getCacheDir() + "cropped" + System.currentTimeMillis();
        File file = new File(filePath);
        Uri destination = Uri.fromFile(file);
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            cameraButton.setImageURI(Crop.getOutput(result));
            cameraButton.setScaleType(ImageView.ScaleType.FIT_XY);
            photoExist = true;
            File file = new File(filePath);
            if(file.exists()) {
                Log.d("lly", "Delete: " + file.delete() + "again" + file.exists());
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveClicked(View view){
        Toast.makeText(getApplicationContext(), getString(R.string.save_message), Toast.LENGTH_SHORT).show();

        ReminderEntry entry = new ReminderEntry();
        entry.setName(itemName.getText().toString());
        entry.setLabel(getResources().getStringArray(R.array.CategorySpinner)[categorySpinner.getSelectedItemPosition()]);
        entry.setExpireDate(mDateAndTime);

        cameraButton.buildDrawingCache();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        cameraButton.getDrawingCache().compress(Bitmap.CompressFormat.PNG, 100, stream);
        entry.setImage(stream.toByteArray());

        (new ReminderEntryAsyncTask(this)).execute(ReminderEntryAsyncTask.INSERT, entry);
        finish();
    }


}
