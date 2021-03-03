package com.example.squareroute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuRATPActivity extends AppCompatActivity {
    private ImageButton metro;
    private ImageButton rer;
    public static final String EXTRA_MESSAGE = "transport";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squareinrealtime);

        metro =  findViewById(R.id.btn_metro);
        rer = findViewById(R.id.btn_rer);

        metro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuRATPActivity.this,LinesActivity.class);
                //intent.putExtra(EXTRA_MESSAGE,new String("metros"));
                intent.putExtra(EXTRA_MESSAGE,new String("buses"));
                startActivity(intent);

            }
        });

        rer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuRATPActivity.this,LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE,new String("rers"));
                startActivity(intent);
            }
        });


    }
}
