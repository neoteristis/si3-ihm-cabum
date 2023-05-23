package com.example.ihm_cabum.view.archieve;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihm_cabum.controller.archieve.EventListAdapter;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;

import org.osmdroid.util.GeoPoint;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();

    //TODO chnage to get from db
    private void fillDb() throws IllegalAccessException {
        (new Accident(this)).getAll(new FirebaseResponse() {
            @Override
            public void notify(FirebaseObject result) {

            }

            @Override
            public void notify(List<FirebaseObject> result) {
                for(FirebaseObject object : result){
                    Accident accident = (Accident) object;
                    System.out.println(accident.getAddress().getLatitude() + "/" + accident.getAddress().getLongitude());
                    eventList.add(
                                    accident
                    );
                }

                ListView listView = (ListView) findViewById(R.id.lisOfAccidents);
                EventListAdapter eventListAdapter = new EventListAdapter(getApplicationContext(), eventList);
                listView.setAdapter(eventListAdapter);
            }
        });
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

    }
}
