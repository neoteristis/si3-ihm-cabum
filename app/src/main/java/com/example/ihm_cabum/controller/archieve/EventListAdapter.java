package com.example.ihm_cabum.controller.archieve;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.controller.api.GoogleAPIController;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.model.Incident;
import com.example.ihm_cabum.view.factory.Factory;
import com.example.ihm_cabum.view.home.HomeActivity;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> eventList;

    private LayoutInflater layoutInflater;

    private final GoogleAPIController googleAPIController;

    public EventListAdapter(Context context, List<Event> eventList) {
        this.eventList = eventList;
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);

        this.googleAPIController = new GoogleAPIController(context);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.activity_accident_list, null);

        TextView type = (TextView) view.findViewById(R.id.accidentType);
        TextView address = (TextView) view.findViewById(R.id.accidentAddress);
        TextView date = (TextView) view.findViewById(R.id.accidentDate);
        ImageView image = (ImageView) view.findViewById(R.id.accidentImage);
        type.setText(eventList.get(i).getLabel());

        String area = googleAPIController.convertCoordinatesToAreaName(eventList.get(i).getAddress().getLatitude(), eventList.get(i).getAddress().getLongitude());
        address.setText(area == null ? eventList.get(i).getAddress().toDoubleString() : area);

        date.setText(eventList.get(i).getFormattedTime());
        image.setImageBitmap(eventList.get(i).getBitmapImage());

        // Convert the image to bytes
        Bitmap bitmapImage = eventList.get(i).getBitmapImage();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();

        LinearLayout layout = view.findViewById(R.id.accidentElement);
        layout.setOnClickListener(view1 -> {
            Factory factory = new Factory();
            Event event;

            EventType eventType = EventType.getFromLabel(type.getText().toString());

            try {
                event = factory.build(
                        context,
                        eventType,
                        eventList.get(i).getDescription(),
                        byteArrayImage,
                        eventList.get(i).getAddress(),
                        eventList.get(i).getTime(),
                        eventList.get(i).getNumberOfApproval()
                );
            } catch (Throwable e) {
                // TODO : change this exception to make it more appropriate
                return;
            }

            // Create the Intent and add the String parameter
            Intent intent = new Intent(context, HomeActivity.class);

            if (Arrays.asList(EventType.accidents()).contains(eventType)) {
                intent.putExtra("event", (Accident) event);
            } else if (Arrays.asList(EventType.incidents()).contains(eventType)) {
                intent.putExtra("event", (Incident) event);
            } else {
                return;
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Start the MainActivity with the Intent
            context.startActivity(intent);
        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            //TODO open a menu for delete and accident copy
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });

        return view;
    }
}
