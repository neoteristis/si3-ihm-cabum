package com.example.ihm_cabum.view.archieve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihm_cabum.controller.archieve.AccidentListAdapter;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.AccidentType;
import com.example.ihm_cabum.R;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private List<Accident> accidentList = new ArrayList<>();

    //TODO chnage to get from db
    private void fillDb() throws IllegalAccessException {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.work);

// Convert Bitmap to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        for (int i = 0; i < 5; i++) {
            accidentList.add(new Accident(this,
                    AccidentType.COLLISION_SINGLE_VEHICLE,
                    "some test description in order to check",
                    byteArray,
                    new GeoPoint(43.64950, 7.00418),
                    new Date(),
                    20
            ));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        try {
            fillDb();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        ListView listView = (ListView) findViewById(R.id.lisOfAccidents);
        AccidentListAdapter accidentListAdapter = new AccidentListAdapter(getApplicationContext(), accidentList);
        listView.setAdapter(accidentListAdapter);

    }
}
