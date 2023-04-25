package com.example.ihm_cabum.view.archieve;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.view.home.HomeActivity;
import com.example.ihm_cabum.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AccidentListAdapter extends BaseAdapter {

    private Context context;
    private List<Accident> accidentList;

    private LayoutInflater layoutInflater;

    public AccidentListAdapter(Context context, List<Accident> accidentList) {
        this.accidentList = accidentList;
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accidentList.size();
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
        type.setText(accidentList.get(i).getTypeOfAccident().getLabel());

        String area = convertCoordinatesToAreaName(accidentList.get(i).getAddress().getLatitude(), accidentList.get(i).getAddress().getLongitude());
        address.setText(area == null ? accidentList.get(i).getAddress().toDoubleString() : area);

        date.setText(accidentList.get(i).getFormattedTime());
        image.setImageBitmap(accidentList.get(i).getBitmapImage());

        LinearLayout layout = view.findViewById(R.id.accidentElement);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the Intent and add the String parameter
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("address", (String) address.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the MainActivity with the Intent
                context.startActivity(intent);
            }
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

    private String convertCoordinatesToAreaName(double latitude, double longitude) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=YOUR_API_KEY";

        final String[] areaName = new String[1];
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() > 0) {
                            JSONObject firstResult = results.getJSONObject(0);
                            areaName[0] = firstResult.getString("formatted_address");
                            // Do something with the area name
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                });

        //async
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
        return areaName[0];
    }

}
