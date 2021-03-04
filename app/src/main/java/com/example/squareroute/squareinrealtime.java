package com.example.squareroute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class squareinrealtime extends AppCompatActivity {

    ImageButton metro,rer,bus,tram;
    public static final String EXTRA_MESSAGE = "transport";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_squareinrealtime);

        metro = findViewById(R.id.btn_metro);
        rer = findViewById(R.id.btn_rer);
        bus = findViewById(R.id.btn_bus);
        tram = findViewById(R.id.btn_tram);

        metro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE,new String("metros"));
                startActivity(intent);
            }
        });
        rer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE,new String("rers"));
                startActivity(intent);
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE,new String("buses"));
                startActivity(intent);
            }
        });
        tram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE,new String("tramways"));
                startActivity(intent);
            }
        });

    }

}