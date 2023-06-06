package com.example.ihm_cabum.view.form;

import static android.content.ContentValues.TAG;

import static com.example.ihm_cabum.controller.notification.NotificationApp.sendAccidentNotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.controller.api.GoogleAPIController;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Incident;
import com.example.ihm_cabum.utils.ImageUtils;
import com.example.ihm_cabum.view.factory.Factory;
import com.example.ihm_cabum.controller.misc.SpinnerAdapter;
import com.example.ihm_cabum.model.DisasterType;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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
    private TextView dateField;
    private EditText timeField;
    private EditText descriptionField;
    private TextView addressField;
    private Button cancelButton;
    private Button saveButton;
    private Button cancelUploadButton;
    private ImageButton uploadCameraButton;
    private ImageButton requestAddressButton;
    private ConstraintLayout layoutUploadFrame;
    private ConstraintLayout layoutUpload;
    private ImageView uploadedImage;

    private ImageButton addPhotoIcon;
    private TextView addPhotoText;

    private DateFormat dateFormat, dateFormatDay, dateFormatHour;
    private Location location;

    private Event eventForUpdate;
    private EventType typeOfEventForUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident);

        Intent intent = this.getIntent();
        if (intent != null) {
            String typeofEvent = intent.getStringExtra("typeForUpdate");
            if (typeofEvent != null || EventType.getFromLabel(typeofEvent) != null) {
                EventType type = EventType.getFromLabel(typeofEvent);
                Event event = intent.getParcelableExtra("eventForUpdate");
                if (event != null && type != null) {
                    this.eventForUpdate = event;
                    this.typeOfEventForUpdate = type;
                }
                String id = intent.getStringExtra("eventIdForUpdate");
                if(id != null && !id.isEmpty() && this.eventForUpdate != null){
                    this.eventForUpdate.setId(id);
                }
            }
        }

        initView();
        setBasicVisibility();
        setBasicValues();
        setBasicListeners();

        if (this.eventForUpdate == null) {
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
        this.addressField = findViewById(R.id.address_addForm);
        this.cancelButton = findViewById(R.id.cancel_button_addFrom);
        this.saveButton = findViewById(R.id.add_button_addForm);
        this.cancelUploadButton = findViewById(R.id.cancel_upload_button_addFrom);
        this.uploadCameraButton = findViewById(R.id.upload_photo_camera_addForm);
        this.requestAddressButton = findViewById(R.id.location_button_addForm);
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
        Calendar calendar1 = Calendar.getInstance();
        this.location = null;

        if (eventForUpdate != null && typeOfEventForUpdate != null) {
            spinnerDisasterType.setSelection((EventType.isAccident(typeOfEventForUpdate)) ? 0 : 1);
            spinnerAccidentType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, (EventType.isAccident(typeOfEventForUpdate)) ? ITEMS_ACCIDENT_TYPE : ITEMS_INCIDENT_TYPE));
            spinnerAccidentType.setSelection((EventType.isAccident(typeOfEventForUpdate)) ?
                    Arrays.stream(EventType.accidents())
                            .collect(Collectors.toList()).indexOf(typeOfEventForUpdate) :
                    Arrays.stream(EventType.incidents())
                            .collect(Collectors.toList()).indexOf(typeOfEventForUpdate));
            this.dateField.setText(eventForUpdate.getFormattedTime().split(" ")[0]);
            this.timeField.setText(eventForUpdate.getFormattedTime().split(" ")[1]);
            this.location = new Location("NonExistantProviderForIHMCabum!");
            this.location.setLatitude(eventForUpdate.getLatitude());
            this.location.setLongitude(eventForUpdate.getLongitude());
            this.setTitle("Update Accident/Incident");

            this.addressField.setText("Loading...");

            new GoogleAPIController(this)
                    .convertCoordinatesToAreaName(location.getLatitude(),location.getLongitude(),a -> this.addressField.setText(a));

            this.descriptionField.setText(eventForUpdate.getDescription());

            if(this.eventForUpdate.getBitmapImage() != null){
                ImageView imageView = findViewById(R.id.uploaded_image_addForm);
                imageView.setVisibility(View.VISIBLE);
                findViewById(R.id.add_photo_icon_addForm).setVisibility(View.INVISIBLE);
                findViewById(R.id.upload_photo_text_addForm).setVisibility(View.INVISIBLE);
                findViewById(R.id.upload_frame_addForm).setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(eventForUpdate.getBitmapImage());
            }

            this.saveButton.setText("Update");
        }
    }

    private void setBasicListeners() {
        this.uploadedImage.setOnClickListener(uploadedImageListener());
        this.calendarOpenButton.setOnClickListener(calendarButtonListener());
        this.spinnerDisasterType.setOnItemSelectedListener(spinnerDisasterTypeListener());
        this.calendar.setOnDateChangeListener(calendarDateListener());
        this.uploadCameraButton.setOnClickListener(uploadCameraButtonListener());
        this.requestAddressButton.setOnClickListener(requestAddressButtonListener());

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
                spinnerAccidentType.setAdapter(new SpinnerAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, List.of((adapterView.getItemAtPosition(i).equals("Accident")) ? ITEMS_ACCIDENT_TYPE : ITEMS_INCIDENT_TYPE)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
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

    private View.OnClickListener requestAddressButtonListener() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        }

        return view -> {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    Log.d("Location", "onLocationChanged: " + location.toString());
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {
                    Log.d("Location", "onStatusChanged: " + s);
                }

                @Override
                public void onProviderEnabled(String s) {
                    Log.d("Location", "onProviderEnabled: " + s);
                }

                @Override
                public void onProviderDisabled(String s) {
                    Log.d("Location", "onProviderDisabled: " + s);
                }
            };

            // Change the addressField to the current location
            this.addressField.setText("Loading...");
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            this.location = location;

            // Get address from location
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() > 0) {
                addressField.setText(addresses.get(0).getAddressLine(0));
            } else {
                addressField.setText("No address found");
            }

            // Stop listening for location updates
            locationManager.removeUpdates(locationListener);
        };
    }

    private View.OnClickListener onSavePressed() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String textAccidentType = spinnerAccidentType.getSelectedItem().toString();
                    String textDescription = descriptionField.getText().toString();
                    String[] valueAddress = addressField.getText().toString().split(",");
                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                    String textTimeDay = dateField.getText().toString();
                    String textTimeHour = timeField.getText().toString();
                    Bitmap bitmap = ((BitmapDrawable) uploadedImage.getDrawable()).getBitmap();
                    byte[] imageInByte = ImageUtils.convertToByteArray(bitmap);
                    Event event = new Factory().build(AddAccidentActivity.this,
                            EventType.getFromLabel(textAccidentType),
                            textDescription,
                            imageInByte,
                            geoPoint,
                            dateFormat.parse(textTimeDay + " " + textTimeHour));

                    if (eventForUpdate != null && typeOfEventForUpdate != null){
                        event.setId(eventForUpdate.getId());
                    }
                    event.save(new FirebaseResponse() {
                        @Override
                        public void notify(FirebaseObject result) {
                            Event event1 = (Event) result;
                            System.out.println("CREATD :" + event1.getId());
                            viewSuccess();
                            sendAccidentNotification(getApplicationContext(), textAccidentType, textDescription, imageInByte, geoPoint);
                            finish();
                        }

                        @Override
                        public void notify(List<FirebaseObject> result) {

                        }

                        @Override
                        public void error(VolleyError volleyError) {
                            System.out.println("VolleyError");
                            for (StackTraceElement stackTraceElement : volleyError.getStackTrace()) {
                                System.out.println(stackTraceElement.toString());
                            }
                            if (volleyError.getMessage() != null)
                                System.out.println("ERROR: " + volleyError.getMessage());
                            NetworkResponse networkResponse = volleyError.networkResponse;
                            if (networkResponse != null && networkResponse.data != null) {
                                String jsonError = new String(networkResponse.data);
                                System.out.println("ERROR: " + jsonError);
                            }
                            viewError();
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    viewError();
                } catch (ParseException e) {
                    e.printStackTrace();
                    viewError();
                } catch (Throwable e) {
                    e.printStackTrace();
                    viewError();
                }

            }
        };
    }

    private void viewError() {
        Toast toast = Toast.makeText(AddAccidentActivity.this, "Les données ne sont invalides !", Toast.LENGTH_LONG);
        toast.show();
    }

    private void viewSuccess() {
        Toast toast = Toast.makeText(AddAccidentActivity.this, (this.eventForUpdate != null) ? "Mise à jour effectuée" : "Évènement ajouté", Toast.LENGTH_LONG);
        toast.show();
    }
}