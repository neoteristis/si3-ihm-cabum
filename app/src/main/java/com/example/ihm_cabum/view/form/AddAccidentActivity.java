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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident);

        TextView header = findViewById(R.id.title_addForm);
        Spinner spinnerDisasterType = findViewById(R.id.accident_incident_menu_addForm);
        Spinner spinnerAccidentType = findViewById(R.id.type_menu_addForm);

        CalendarView calendar = findViewById(R.id.calendar_addForm);
        ImageButton calendarButton = findViewById(R.id.callendar_button_addForm);
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());

        EditText dateField = findViewById(R.id.date_addForm);
        EditText timeField = findViewById(R.id.time_addForm);

        Button cancelButton = findViewById(R.id.cancel_button_addFrom);
        Button saveButton = findViewById(R.id.add_button_addForm);

        Button cancelUploadButton = findViewById(R.id.cancel_upload_button_addFrom);
        ImageButton uploadCameraButton = findViewById(R.id.upload_photo_camera_addForm);

        ConstraintLayout layoutUploadFrame = findViewById(R.id.upload_frame_addForm);
        ConstraintLayout layoutUpload = findViewById(R.id.add_picture_addForm);

        ImageView imageView = findViewById(R.id.uploaded_image_addForm);
        imageView.setVisibility(View.INVISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setVisibility(View.INVISIBLE);
                findViewById(R.id.add_photo_addForm).setVisibility(View.VISIBLE);
                findViewById(R.id.upload_photo_text_addForm).setVisibility(View.VISIBLE);
            }
        });

        spinnerDisasterType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS_DISASTER));
        spinnerAccidentType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS_ACCIDENT_TYPE));

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
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.setVisibility(calendar.getVisibility() == View.INVISIBLE ? View.VISIBLE : View.INVISIBLE);
            }
        });

        spinnerDisasterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                header.setText(DEFAULT_HEADER_BEGINNING + adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        calendar.setVisibility(View.INVISIBLE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                dateField.setText(dateFormat.format(new Date(year, month, day)));
                calendar.setVisibility(View.INVISIBLE);
            }
        });

        dateField.setText(dateFormat.format(new Date()));
        Calendar calendar1 = Calendar.getInstance();
        timeField.setText(calendar1.get(Calendar.HOUR_OF_DAY) + ":" + calendar1.get(Calendar.MINUTE));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        layoutUploadFrame.setVisibility(View.INVISIBLE);
        layoutUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutUploadFrame.setVisibility(View.VISIBLE);
            }
        });
        cancelUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutUploadFrame.setVisibility(View.INVISIBLE);
            }
        });

        uploadCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                requestPermission();

                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 1);
                }
            }
        });
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
            findViewById(R.id.add_photo_addForm).setVisibility(View.INVISIBLE);
            findViewById(R.id.upload_photo_text_addForm).setVisibility(View.INVISIBLE);
            findViewById(R.id.upload_frame_addForm).setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
    }
}