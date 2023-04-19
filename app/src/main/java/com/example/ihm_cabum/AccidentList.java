package com.example.ihm_cabum;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ihm_cabum.ui.profile.ProfileActivity;

public class AccidentList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_list);

        TextView address = findViewById(R.id.accidentAddress);
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the Intent and add the String parameter
                Intent intent = new Intent(AccidentList.this, MainActivity.class);
                intent.putExtra("address", (String) address.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the MainActivity with the Intent
                startActivity(intent);
            }
        });
    }
}