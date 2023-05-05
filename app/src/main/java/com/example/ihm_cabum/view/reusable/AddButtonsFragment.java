package com.example.ihm_cabum.view.reusable;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.view.form.AddAccidentActivity;
import com.example.ihm_cabum.view.profile.ProfileActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddButtonsFragment extends Fragment {

    private boolean isAddClicked = false;

    public AddButtonsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_buttons, container, false);

        Animation fromBottomAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        Animation toBottomAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);
        Animation rotateCloseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_animation);
        Animation rotateOpenAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_animation);

        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        FloatingActionButton accident = view.findViewById(R.id.accident);
        FloatingActionButton incident = view.findViewById(R.id.incident);

        if(addButton == null || accident == null || incident == null) {
            return view;
        }

        accident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddAccidentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        ConstraintLayout constraintLayout = view.findViewById(R.id.tabAdd);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAddClicked) {
                    accident.setVisibility(View.INVISIBLE);
                    incident.setVisibility(View.INVISIBLE);

                    constraintLayout.setVisibility(View.INVISIBLE);

                    accident.startAnimation(toBottomAnimation);
                    incident.startAnimation(toBottomAnimation);

                    addButton.startAnimation(rotateCloseAnimation);
                } else {
                    constraintLayout.setVisibility(View.VISIBLE);

                    accident.setVisibility(View.VISIBLE);
                    incident.setVisibility(View.VISIBLE);

                    accident.startAnimation(fromBottomAnimation);
                    incident.startAnimation(fromBottomAnimation);

                    addButton.startAnimation(rotateOpenAnimation);
                }
                isAddClicked = !isAddClicked;
            }
        });

        constraintLayout.setVisibility(View.INVISIBLE);

        return view;
    }
}