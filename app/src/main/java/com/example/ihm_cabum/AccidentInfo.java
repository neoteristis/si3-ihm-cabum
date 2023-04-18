package com.example.ihm_cabum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        }

        Button closeButton = view.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        Button approveButton = view.findViewById(R.id.approve);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accident.approve();
            }
        });

        Button disApproveButton = view.findViewById(R.id.disapprove_accident);
        approveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accident.disApprove();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}