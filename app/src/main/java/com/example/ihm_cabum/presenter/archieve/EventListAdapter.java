package com.example.ihm_cabum.presenter.archieve;

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
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.presenter.reusable.OpenStreetMapAPI;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.Event;
import com.example.ihm_cabum.model.EventType;
import com.example.ihm_cabum.model.Incident;
import com.example.ihm_cabum.model.patterns.factory.Factory;
import com.example.ihm_cabum.presenter.form.AddAccidentActivity;
import com.example.ihm_cabum.presenter.home.activity.HomeActivity;
import com.example.ihm_cabum.model.volley.FirebaseObject;
import com.example.ihm_cabum.model.volley.FirebaseResponse;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Event> eventList;

    private final LayoutInflater layoutInflater;

    private AlertDialog alertDialog;

    private final OpenStreetMapAPI openStreetMapAPI;
    private final Activity parentActivity;

    public EventListAdapter(Context context, List<Event> eventList, Activity parentActivity) {
        this.eventList = eventList;
        this.context = context;
        this.parentActivity = parentActivity;

        this.layoutInflater = LayoutInflater.from(context);

        this.openStreetMapAPI = new OpenStreetMapAPI(context);
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

        TextView type = view.findViewById(R.id.accidentType);
        TextView date = view.findViewById(R.id.accidentDate);
        ImageView image = view.findViewById(R.id.accidentImage);
        type.setText(eventList.get(i).getLabel());

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
                        eventList.get(i).getId(),
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

        //TODO open a menu for delete and accident copy
        layout.setOnLongClickListener(v -> true);

        //This part is about edit and deletion of event
        ImageButton editButton = view.findViewById(R.id.edit_event);
        ImageButton deleteButton = view.findViewById(R.id.delete_event);

        editButton.setOnClickListener(view13 -> {
            Factory factory = new Factory();
            Event event;

            EventType eventType = EventType.getFromLabel(type.getText().toString());

            try {
                event = factory.build(
                        context,
                        eventList.get(i).getId(),
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
            Intent intent = new Intent(context, AddAccidentActivity.class);

            if (Arrays.asList(EventType.accidents()).contains(eventType)) {
                intent.putExtra("eventForUpdate", (Accident) event);
                intent.putExtra("typeForUpdate", ((Accident) event).getTypeOfAccident().getLabel());
            } else if (Arrays.asList(EventType.incidents()).contains(eventType)) {
                intent.putExtra("eventForUpdate", (Incident) event);
                intent.putExtra("typeForUpdate", ((Incident) event).getTypeOfIncident().getLabel());
            } else {
                return;
            }
            intent.putExtra("eventIdForUpdate",event.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Start the MainActivity with the Intent
            context.startActivity(intent);
        });

        deleteButton.setOnClickListener(view12 -> {
            Factory factory = new Factory();
            Event event;

            EventType eventType = EventType.getFromLabel(type.getText().toString());

            try {
                event = factory.build(
                        context,
                        eventList.get(i).getId(),
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

            showConfirmationDialog(event);
        });

        return view;
    }

    private void showConfirmationDialog(Event event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        LayoutInflater inflater = LayoutInflater.from(parentActivity);
        View dialogView = inflater.inflate(R.layout.dialog_window_confirm_deletion, null);
        builder.setView(dialogView);

        TextView titleTextView = dialogView.findViewById(R.id.dialog_title);
        Button cancelButton = dialogView.findViewById(R.id.btn_cancel);
        Button confirmButton = dialogView.findViewById(R.id.btn_delete);

        // Set the title and button click listeners
        titleTextView.setText(R.string.delete_event_confirmation);
        cancelButton.setOnClickListener(v -> {
            // Cancel button clicked
            alertDialog.dismiss();
        });
        confirmButton.setOnClickListener(v -> {
            try {
                event.delete(new FirebaseResponse() {
                    @Override
                    public void notify(FirebaseObject result) {
                        Event event1 = (Event) result;
                        System.out.println("CREATD :" + event1.getId());
                        parentActivity.finish();
                        parentActivity.startActivity(parentActivity.getIntent());
                        viewSuccess();
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
            } catch (JSONException e) {
                e.printStackTrace();
                viewError();
            }
            alertDialog.dismiss();
        });

        alertDialog = builder.create(); // Assign the alertDialog to the member variable
        alertDialog.show();
    }

    private void viewError() {
        Toast toast = Toast.makeText(context, "Deletion failed", Toast.LENGTH_LONG);
        toast.show();
    }

    private void viewSuccess() {
        Toast toast = Toast.makeText(context, "Deletion success", Toast.LENGTH_LONG);
        toast.show();
    }
}
