package com.example.ihm_cabum.view.home;

import static com.example.ihm_cabum.controller.notification.NotificationApp.sendNotification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.R;

public class AccidentInfo extends Fragment {
    private Accident accident;

    public AccidentInfo() {
        // Required empty public constructor
        this.accident = null;
    }

    public AccidentInfo(Accident accident) {
        this.accident = accident;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accident_info,container,false);

        if(accident != null) {
            ((ImageView) view.findViewById(R.id.image)).setImageBitmap(accident.getBitmapImage());
            ((TextView) view.findViewById(R.id.description)).setText(accident.getDescription());
            ((TextView) view.findViewById(R.id.type)).setText(accident.getTypeOfAccident().getLabel());
            ((TextView) view.findViewById(R.id.description)).setText(accident.getDescription());
            ((TextView) view.findViewById(R.id.date)).setText(accident.getFormattedTime());

            sendNotification(getContext(), "Accident reported", accident.getTypeOfAccident().getLabel());

            Button approveButton = view.findViewById(R.id.approve);
            approveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accident.approve();
                }
            });

            Button disApproveButton = view.findViewById(R.id.disapprove_accident);
            disApproveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accident.disApprove();
                }
            });
        }

        Button closeButton = view.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}