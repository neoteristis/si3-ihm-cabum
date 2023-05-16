package com.example.ihm_cabum.controller.archieve;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ihm_cabum.controller.api.GoogleAPIController;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.view.home.HomeActivity;
import com.example.ihm_cabum.R;

import java.util.List;

public class AccidentListAdapter extends BaseAdapter {

    private Context context;
    private List<Accident> accidentList;

    private LayoutInflater layoutInflater;

    private final GoogleAPIController googleAPIController;

    public AccidentListAdapter(Context context, List<Accident> accidentList) {
        this.accidentList = accidentList;
        this.context = context;

        this.layoutInflater = LayoutInflater.from(context);

        this.googleAPIController = new GoogleAPIController(context);
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

        String area = googleAPIController.convertCoordinatesToAreaName(accidentList.get(i).getAddress().getLatitude(), accidentList.get(i).getAddress().getLongitude());
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
}
