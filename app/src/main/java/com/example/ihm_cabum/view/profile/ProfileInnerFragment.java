package com.example.ihm_cabum.view.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.ihm_cabum.R;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileInnerFragment extends PreferenceFragmentCompat {
        private View rootView;

        public ProfileInnerFragment(View view) {
            this.rootView = view;
        }
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.profile_frame, rootKey);

            MultiSelectListPreference transportPreference = findPreference("transports");
            transportPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object value) {


                    List<Integer> icons = List.of(R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4);
                    Map<String, Integer> transport_map = Map.of(
                            "Car", R.drawable.ic_car,
                            "Motorbike", R.drawable.ic_motorbike,
                            "Bicycle", R.drawable.ic_bicycle,
                            "Walk", R.drawable.ic_walk);

                    // Remove all icons
                    for (int icon : icons){
                        ImageView view = rootView.findViewById(icon);
                        view.setImageResource(0);
                    }

                    // Replace with new icons for selected transports
                    Set<String> transports = (Set<String>) value;
                    int i=0;
                    for (String transport : transports){
                        ImageView view = rootView.findViewById(icons.get(i));
                        view.setImageResource(transport_map.get(transport));
                        i++;
                    }
                    return true;
                }
            });

        }
    }

