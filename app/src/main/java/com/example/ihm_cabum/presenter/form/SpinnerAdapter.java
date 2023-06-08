package com.example.ihm_cabum.presenter.form;


import android.app.UiModeManager;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        TextView textView = view.findViewById(android.R.id.text1);

        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        if(uiModeManager == null) {
            return view;
        }

        if (uiModeManager.getNightMode() != UiModeManager.MODE_NIGHT_YES) {
            textView.setTextColor(Color.BLACK);
        } else {
            textView.setTextColor(Color.WHITE);
        }

        return view;
    }
}
