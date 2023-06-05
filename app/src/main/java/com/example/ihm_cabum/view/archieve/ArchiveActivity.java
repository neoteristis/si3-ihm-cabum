package com.example.ihm_cabum.view.archieve;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.controller.archieve.EventListAdapter;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.volley.FirebaseObject;
import com.example.ihm_cabum.volley.FirebaseResponse;

import java.util.ArrayList;
import java.util.List;

public class ArchiveActivity extends AppCompatActivity {

    private List<Event> eventList = new ArrayList<>();

    private void fillDb() throws IllegalAccessException {
        (new Accident(this)).getAll(new FirebaseResponse() {
            @Override
            public void notify(FirebaseObject result) {

            }

            @Override
            public void notify(List<FirebaseObject> result) {
                List<Event> tmpEventList = new ArrayList<>();
                for(FirebaseObject object : result){
                    Accident accident = (Accident) object;
                    System.out.println(accident.getAddress().getLatitude() + "/" + accident.getAddress().getLongitude());
                    tmpEventList.add(
                                    accident
                    );
                }

                eventList = tmpEventList;

                ListView listView = (ListView) findViewById(R.id.lisOfAccidents);
                EventListAdapter eventListAdapter = new EventListAdapter(ArchiveActivity.this.getApplicationContext(), eventList,ArchiveActivity.this);
                listView.setAdapter(eventListAdapter);
            }

            @Override
            public void error(VolleyError volleyError) {
                System.out.println("ERROR: " + volleyError.getMessage());
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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            fillDb();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
