package com.example.ihm_cabum.ui.navbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ihm_cabum.MainActivity;
import com.example.ihm_cabum.R;
import com.example.ihm_cabum.ui.archieve.ArchiveActivity;
import com.example.ihm_cabum.ui.home.HomeActivity;
import com.example.ihm_cabum.ui.profile.ProfileActivity;
import com.example.ihm_cabum.ui.profile.ProfileFragment;

public class NavbarFragment extends Fragment {
    public NavbarFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navbar,container,false);

        Button homeBtn = view.findViewById(R.id.navigation_home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Button archiveBtn = view.findViewById(R.id.navigation_archive);
        archiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ArchiveActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        Button profileBtn = view.findViewById(R.id.navigation_profile);
        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        return view;
    }
}
