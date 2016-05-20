package cs165.edu.dartmouth.cs.beforespoiled;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntry;
import cs165.edu.dartmouth.cs.beforespoiled.database.ReminderEntryAsyncTask;

public class ManualAddItemActivity extends Activity {

    public static final int TAKE_PHOTO_REQUEST_CODE = 0;
    private static final String URI_INSTANCE_STATE_KEY = "saved_uri";
    private TextView mDisplayDateTime;
    private Calendar mDateAndTime = Calendar.getInstance();
    private EditText itemName;
    private ImageButton cameraButton;
    private Spinner categorySpinner;
    private Uri mImageCaptureUri;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_add_item);
        //User type item name
        itemName = (EditText) findViewById(R.id.editTextName);
        itemName.clearFocus();
        //User change expire date by clicking image button.
        mDisplayDateTime = (TextView) findViewById(R.id.DateDisplayView);
        updateDateAndTimeDisplay();
        //
        cameraButton = (ImageButton) findViewById(R.id.ShowCameraButton);

        loadSnap();

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
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                         cameraButton.setImageResource(R.drawable.vege);
                        break;
                    case 1:
                        cameraButton.setImageResource(R.drawable.fruit);
                        break;
                    case 2:
                        cameraButton.setImageResource(R.drawable.bread);
                        break;
                    case 3:
                        cameraButton.setImageResource(R.drawable.milk);
                        break;
                    case 4:
                        cameraButton.setImageResource(R.drawable.spices);
                        break;
                    case 5:
                        cameraButton.setImageResource(R.drawable.frozen);
                        break;
                    case 6:
                        cameraButton.setImageResource(R.drawable.grain);
                        break;
                    case 7:
                        cameraButton.setImageResource(R.drawable.snack);
                        break;
                    case 8:
                        cameraButton.setImageResource(R.drawable.beverage);
                        break;
                    case 9:
                        cameraButton.setImageResource(R.drawable.fish);
                }
                cameraButton.setScaleType(ImageView.ScaleType.FIT_XY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               cameraButton.setImageResource(R.drawable.fish);
            }
        });

        loadSnap();




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
//                if (isTakenFromCamera) {
                    File f = new File(mImageCaptureUri.getPath());
                    if (f.exists())
                        f.delete();
//                }
                break;


        }
    }

    private void beginCrop(Uri source) {
        filePath = getCacheDir() + "cropped" + System.currentTimeMillis();
        File file = new File(filePath);


        Uri destination = Uri.fromFile(file);

//        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            cameraButton.setImageURI(Crop.getOutput(result));
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

    private void loadSnap() {
        // Load profile photo from internal storage
        try {
            FileInputStream fis = openFileInput(getString(R.string.photo_name));
            Bitmap bmap = BitmapFactory.decodeStream(fis);
            cameraButton.setImageBitmap(bmap);
            cameraButton.setScaleType(ImageView.ScaleType.FIT_XY);
            fis.close();
        } catch (IOException e) {
            // Default profile photo if no photo saved before.
            cameraButton.setImageResource(R.drawable.fish);
            cameraButton.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

    private void saveSnap() {
        // Commit all the changes into preference file
        // Save profile image into internal storage.
        cameraButton.buildDrawingCache();
        Bitmap bmap = cameraButton.getDrawingCache();
        try {
            FileOutputStream fos = openFileOutput(
                    getString(R.string.photo_name), MODE_PRIVATE);
            bmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    //Not done for on Save clicked!!!!

    public void onSaveClicked(View view){
        Toast.makeText(getApplicationContext(), getString(R.string.save_message), Toast.LENGTH_SHORT).show();

        ReminderEntry entry = new ReminderEntry();
        entry.setName(itemName.getText().toString());
        entry.setLabel(getResources().getStringArray(R.array.CategorySpinner)[categorySpinner.getSelectedItemPosition()]);
        entry.setExpireDate(mDateAndTime);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ((BitmapDrawable) cameraButton.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 0, stream);
        entry.setImage(stream.toByteArray());

        (new ReminderEntryAsyncTask(this)).execute(ReminderEntryAsyncTask.INSERT, entry);
        finish();
    }


}
