package com.example.ihm_cabum.view.form;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import android.Manifest;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.AccidentType;
import com.example.ihm_cabum.model.DisasterType;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.IntStream;

public class AddAccidentActivity extends AppCompatActivity {

    private final static String[] ITEMS_DISASTER = Arrays.stream(DisasterType.values()).map(DisasterType::getLabel).toArray(String[]::new);
    private final static String[] ITEMS_ACCIDENT_TYPE = Arrays.stream(AccidentType.values()).map(AccidentType::getLabel).toArray(String[]::new);

    private final static String DEFAULT_HEADER_BEGINNING = "Create an ";
    private final static int REQUEST_IMAGE_CAPTURE = 1;

    private TextView header;
    private Spinner spinnerDisasterType;
    private Spinner spinnerAccidentType;
    private CalendarView calendar;
    private ImageButton calendarOpenButton;
    private EditText dateField;
    private EditText timeField;
    private Button cancelButton;
    private Button saveButton;
    private Button cancelUploadButton;
    private ImageButton uploadCameraButton;
    private ConstraintLayout layoutUploadFrame;
    private ConstraintLayout layoutUpload;
    private ImageView uploadedImage;

    private ImageButton addPhotoIcon;
    private TextView addPhotoText;

    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident);

        initView();
        setBasicVisibility();
        setBasicValues();
        setBasicListeners();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selected = extras.getString("type");
            header.setText(DEFAULT_HEADER_BEGINNING + selected);

            spinnerDisasterType.setSelection(
                    IntStream.range(0, ITEMS_DISASTER.length)
                            .filter(i -> ITEMS_DISASTER[i].toLowerCase().equals(selected))
                            .findFirst()
                            .orElse(0)
            );
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Upload the image bitmap to your app
            ImageView imageView = findViewById(R.id.uploaded_image_addForm);
            imageView.setVisibility(View.VISIBLE);
            findViewById(R.id.add_photo_icon_addForm).setVisibility(View.INVISIBLE);
            findViewById(R.id.upload_photo_text_addForm).setVisibility(View.INVISIBLE);
            findViewById(R.id.upload_frame_addForm).setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void initView() {
        this.header = findViewById(R.id.title_addForm);
        this.spinnerDisasterType = findViewById(R.id.accident_incident_menu_addForm);
        this.spinnerAccidentType = findViewById(R.id.type_menu_addForm);
        this.calendar = findViewById(R.id.calendar_addForm);
        this.calendarOpenButton = findViewById(R.id.callendar_button_addForm);
        this.dateField = findViewById(R.id.date_addForm);
        this.timeField = findViewById(R.id.time_addForm);
        this.cancelButton = findViewById(R.id.cancel_button_addFrom);
        this.saveButton = findViewById(R.id.add_button_addForm);
        this.cancelUploadButton = findViewById(R.id.cancel_upload_button_addFrom);
        this.uploadCameraButton = findViewById(R.id.upload_photo_camera_addForm);
        this.layoutUploadFrame = findViewById(R.id.upload_frame_addForm);
        this.layoutUpload = findViewById(R.id.add_picture_addForm);
        this.uploadedImage = findViewById(R.id.uploaded_image_addForm);
        this.addPhotoIcon = findViewById(R.id.add_photo_icon_addForm);
        this.addPhotoText = findViewById(R.id.upload_photo_text_addForm);

        this.dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
    }

    private void setBasicVisibility() {
        uploadedImage.setVisibility(View.INVISIBLE);
        calendar.setVisibility(View.INVISIBLE);
        layoutUploadFrame.setVisibility(View.INVISIBLE);
    }

    private void setBasicValues() {
        this.spinnerDisasterType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS_DISASTER));
        this.spinnerAccidentType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS_ACCIDENT_TYPE));

        this.dateField.setText(dateFormat.format(new Date()));
        Calendar calendar1 = Calendar.getInstance();
        this.timeField.setText(calendar1.get(Calendar.HOUR_OF_DAY) + ":" + calendar1.get(Calendar.MINUTE));
    }

    private void setBasicListeners() {
        this.uploadedImage.setOnClickListener(uploadedImageListener());
        this.calendarOpenButton.setOnClickListener(calendarButtonListener());
        this.spinnerDisasterType.setOnItemSelectedListener(spinnerDisasterTypeListener());
        this.calendar.setOnDateChangeListener(calendarDateListener());
        this.uploadCameraButton.setOnClickListener(uploadCameraButtonListener());

        //easy operations
        this.cancelButton.setOnClickListener(view -> onBackPressed());
        this.layoutUpload.setOnClickListener(view -> layoutUploadFrame.setVisibility(View.VISIBLE));
        this.cancelUploadButton.setOnClickListener(view -> layoutUploadFrame.setVisibility(View.INVISIBLE));
    }

    private View.OnClickListener uploadedImageListener() {
        return view -> {
            uploadedImage.setVisibility(View.INVISIBLE);
            addPhotoIcon.setVisibility(View.VISIBLE);
            addPhotoText.setVisibility(View.VISIBLE);
        };
    }

    private View.OnClickListener calendarButtonListener() {
        return view ->
                calendar.setVisibility(
                        calendar.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE
                );
    }

    private View.OnClickListener uploadCameraButtonListener() {
        return view -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            requestPermission();

            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        };
    }

    private AdapterView.OnItemSelectedListener spinnerDisasterTypeListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                header.setText(DEFAULT_HEADER_BEGINNING + adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };
    }

    private CalendarView.OnDateChangeListener calendarDateListener() {
        return (calendarView, year, month, day) -> {
            dateField.setText(dateFormat.format(new Date(year, month, day)));
            calendar.setVisibility(View.INVISIBLE);
        };
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }
    }
}