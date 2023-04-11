package com.example.ihm_cabum.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ihm_cabum.R;
import com.example.ihm_cabum.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.profile_page, new ProfileFragment_utils.ProfileInnerFragment())
                .commit();

        return view;
    }

}
