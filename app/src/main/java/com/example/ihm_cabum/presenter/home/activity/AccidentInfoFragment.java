package com.example.ihm_cabum.presenter.home.activity;

import static com.example.ihm_cabum.presenter.notification.NotificationApp.sendAccidentNotification;
import static com.example.ihm_cabum.presenter.profile.ProfileInnerFragment.notificationsOn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.model.Accident;
import com.example.ihm_cabum.model.volley.FirebaseFireAndForget;

import org.json.JSONException;

import java.lang.reflect.InvocationTargetException;

public class AccidentInfoFragment extends Fragment {
    private final Accident accident;

    public AccidentInfoFragment() {
        // Required empty public constructor
        this.accident = null;
    }

    public AccidentInfoFragment(Accident accident) {
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

            if (notificationsOn)
                sendAccidentNotification(getContext(), accident);

            Button approveButton = view.findViewById(R.id.approve);
            approveButton.setOnClickListener(view13 -> {
                accident.approve();
                try {
                    accident.save(new FirebaseFireAndForget());
                } catch (InvocationTargetException | IllegalAccessException | JSONException e) {
                    throw new RuntimeException(e);
                }
            });

            Button disApproveButton = view.findViewById(R.id.disapprove_accident);
            disApproveButton.setOnClickListener(view12 -> {
                accident.disApprove();
                try {
                    accident.save(new FirebaseFireAndForget());
                } catch (InvocationTargetException | IllegalAccessException | JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        Button closeButton = view.findViewById(R.id.close);
        closeButton.setOnClickListener(view1 -> getActivity().onBackPressed());

        // Inflate the layout for this fragment
        return view;
    }
}