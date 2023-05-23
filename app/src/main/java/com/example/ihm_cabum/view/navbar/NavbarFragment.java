package com.example.ihm_cabum.view.navbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.ihm_cabum.view.form.AddAccidentActivity;
import com.example.ihm_cabum.view.home.HomeActivity;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.view.archieve.ArchiveActivity;
import com.example.ihm_cabum.view.profile.ProfileActivity;

public class NavbarFragment extends Fragment {
    public NavbarFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar,container,false);

        FragmentActivity activity = getActivity();
        Button homeBtn = view.findViewById(R.id.navigation_home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                if(activity instanceof AddAccidentActivity || activity instanceof HomeActivity) {
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                } else {
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        });
        Button archiveBtn = view.findViewById(R.id.navigation_archive);
        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ArchiveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                if(activity instanceof ProfileActivity) {
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else if(activity instanceof HomeActivity) {
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                }
            }
        });
        Button profileBtn = view.findViewById(R.id.navigation_profile);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                if(activity instanceof AddAccidentActivity || activity instanceof ProfileActivity) {
                    getActivity().overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
                } else {
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });


        return view;
    }
}
