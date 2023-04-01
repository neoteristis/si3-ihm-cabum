package com.example.ihm_cabum.ui.profile;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.databinding.FragmentProfileBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private static View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.profile_page, new ProfileInnerFragment())
                .commit();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static class ProfileInnerFragment extends PreferenceFragmentCompat {
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

}
