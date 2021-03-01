package com.example.squareroute;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class squareinrealtime extends AppCompatActivity {

    ImageButton metro,rer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_squareinrealtime);

        metro = findViewById(R.id.btn_metro);
        rer = findViewById(R.id.btn_rer);

        metro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  startActivity(new Intent(getApplicationContext(),squareinrealtime.class));
            }
        });
        rer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  startActivity(new Intent(getApplicationContext(),squareinrealtime.class));
            }
        });
    }

}