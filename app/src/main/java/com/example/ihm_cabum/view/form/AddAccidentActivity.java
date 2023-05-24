package com.example.ihm_cabum.view.form;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.utils.ImageUtils;
import com.example.ihm_cabum.view.factory.Factory;
import com.example.ihm_cabum.model.DisasterType;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.IntStream;

public class AddAccidentActivity extends AppCompatActivity {

    private final static String[] ITEMS_DISASTER = Arrays.stream(DisasterType.values()).map(DisasterType::getLabel).toArray(String[]::new);
    private final static String[] ITEMS_ACCIDENT_TYPE = Arrays.stream(EventType.accidents()).map(EventType::getLabel).toArray(String[]::new);
    private final static String[] ITEMS_INCIDENT_TYPE = Arrays.stream(EventType.incidents()).map(EventType::getLabel).toArray(String[]::new);
    private final static String DEFAULT_HEADER_BEGINNING = "Create an ";
    private final static int REQUEST_IMAGE_CAPTURE = 1;

    private TextView header;
    private Spinner spinnerDisasterType;
    private Spinner spinnerAccidentType;
    private CalendarView calendar;
    private ImageButton calendarOpenButton;
    private EditText dateField;
    private EditText timeField;
    private EditText descriptionField;
    private EditText addressField;
    private Button cancelButton;
    private Button saveButton;
    private Button cancelUploadButton;
    private ImageButton uploadCameraButton;
    private ConstraintLayout layoutUploadFrame;
    private ConstraintLayout layoutUpload;
    private ImageView uploadedImage;

    private ImageButton addPhotoIcon;
    private TextView addPhotoText;

    private DateFormat dateFormat, dateFormatDay,dateFormatHour;

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

        this.dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.dateFormatDay = new SimpleDateFormat("yyyy/MM/dd");
        this.dateFormatHour = new SimpleDateFormat("HH:mm:ss");

        this.descriptionField = findViewById(R.id.editText);
        this.addressField = findViewById(R.id.address_addForm);
    }

    private void setBasicVisibility() {
        uploadedImage.setVisibility(View.INVISIBLE);
        calendar.setVisibility(View.INVISIBLE);
        layoutUploadFrame.setVisibility(View.INVISIBLE);
    }

    private void setBasicValues() {
        this.spinnerDisasterType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS_DISASTER));
        this.spinnerAccidentType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ITEMS_INCIDENT_TYPE));

        this.dateField.setText(dateFormatDay.format(new Date()));
        this.timeField.setText(dateFormatHour.format(new Date()));
    }

    private void setBasicListeners() {
        this.uploadedImage.setOnClickListener(uploadedImageListener());
        this.calendarOpenButton.setOnClickListener(calendarButtonListener());
        this.spinnerDisasterType.setOnItemSelectedListener(spinnerDisasterTypeListener());
        this.calendar.setOnDateChangeListener(calendarDateListener());
        this.uploadCameraButton.setOnClickListener(uploadCameraButtonListener());

        //easy operations
        this.cancelButton.setOnClickListener(view -> onBackPressed());
        this.saveButton.setOnClickListener(onSavePressed());
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
                spinnerAccidentType.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, (adapterView.getItemAtPosition(i).equals("Accident")) ? ITEMS_ACCIDENT_TYPE : ITEMS_INCIDENT_TYPE));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        };
    }

    private CalendarView.OnDateChangeListener calendarDateListener() {
        return (calendarView, year, month, day) -> {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.set(year, month, day);
            dateField.setText(dateFormatDay.format(calendar1.getTime()));
            calendar.setVisibility(View.INVISIBLE);
        };
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }
    }

    private View.OnClickListener onSavePressed(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String textAccidentType = spinnerAccidentType.getSelectedItem().toString();
                    String textDescription = descriptionField.getText().toString();
                    String[] valueAddress = addressField.getText().toString().split(",");
                    GeoPoint geoPoint = new GeoPoint(Double.parseDouble(valueAddress[0]),Double.parseDouble(valueAddress[1]));
                    String textTimeDay = dateField.getText().toString();
                    String textTimeHour = timeField.getText().toString();
                    Bitmap bitmap = ((BitmapDrawable) uploadedImage.getDrawable()).getBitmap();
                    byte[] imageInByte = ImageUtils.convertToByteArray(bitmap);
                    Event event = new Factory().build(AddAccidentActivity.this,
                            EventType.getFromLabel(textAccidentType),
                            textDescription,
                            imageInByte,
                            geoPoint,
                            dateFormat.parse(textTimeDay+" "+textTimeHour));
                    event.save(new FirebaseResponse() {
                        @Override
                        public void notify(FirebaseObject result) {
                            Event event1 = (Event) result;
                            System.out.println("CREATD :" + event1.getId());
                        }

                        @Override
                        public void notify(List<FirebaseObject> result) {

                        }

                        @Override
                        public void error(VolleyError volleyError) {
                            System.out.println("ERROR: " + volleyError.getMessage());
                        }
                    });
                } catch (IllegalAccessException e) {
                    viewError();
                } catch (ParseException e) {
                    viewError();
                } catch (Throwable e) {
                    viewError();
                }
            }
        };
    }

    private void viewError(){
        Toast toast = Toast.makeText(AddAccidentActivity.this,"Les données ne sont invalides !", Toast.LENGTH_LONG);
        toast.show();
    }
}