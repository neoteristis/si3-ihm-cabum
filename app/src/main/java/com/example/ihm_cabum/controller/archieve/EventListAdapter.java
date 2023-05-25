package com.example.ihm_cabum.controller.archieve;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.controller.api.GoogleAPIController;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.model.Incident;
import com.example.ihm_cabum.view.archieve.ArchiveActivity;
import com.example.ihm_cabum.view.factory.Factory;
import com.example.ihm_cabum.view.home.HomeActivity;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private Context context;
    private List<Event> eventList;

    private LayoutInflater layoutInflater;

    private AlertDialog alertDialog;

    private final GoogleAPIController googleAPIController;
    private Activity parentActivity;

    public EventListAdapter(Context context, List<Event> eventList, Activity parentActivity) {
        this.eventList = eventList;
        this.context = context;
        this.parentActivity = parentActivity;

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
        TextView date = (TextView) view.findViewById(R.id.accidentDate);
        ImageView image = (ImageView) view.findViewById(R.id.accidentImage);
        type.setText(eventList.get(i).getLabel());

        String area = googleAPIController.convertCoordinatesToAreaName(eventList.get(i).getAddress().getLatitude(), eventList.get(i).getAddress().getLongitude());

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

        //This part is about edit and deletion of event
        ImageButton editButton = view.findViewById(R.id.edit_event);
        ImageButton deleteButton = view.findViewById(R.id.delete_event);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Factory factory = new Factory();
                Event event;

                EventType eventType = EventType.getFromLabel(type.getText().toString());

                try {
                    event = factory.build(
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
                //TODO : Change null by the activity to edit an accident/incident
                Intent intent = new Intent(context, null);

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
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(i);
            }
        });

        return view;
    }

    private void showConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        LayoutInflater inflater = LayoutInflater.from(parentActivity);
        View dialogView = inflater.inflate(R.layout.dialog_window_confirm_deletion, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
        Button confirmButton = dialogView.findViewById(R.id.btn_delete);

        // Set the title and button click listeners
        titleTextView.setText(R.string.delete_event_confirmation);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel button clicked
                alertDialog.dismiss();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete button clicked
                // Perform deletion logic here using the position
                // ...

                // Dismiss the dialog
                // TODO: Do the deletion here
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create(); // Assign the alertDialog to the member variable
        alertDialog.show();
    }
}
