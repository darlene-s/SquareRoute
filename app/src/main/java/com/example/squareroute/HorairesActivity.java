package com.example.squareroute;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HorairesActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_horaires);
        textView = (TextView) findViewById(R.id.horaires);

        Intent intent = getIntent();
        final String line = intent.getStringExtra(LinesActivity.EXTRA_MESSAGE_LINE);
        final String transport = intent.getStringExtra(LinesActivity.EXTRA_MESSAGE_TRANSPORT);
        final String station = intent.getStringExtra(StationsActivity.EXTRA_MESSAGE_STATION);

        RequestAPI(line,transport,station);


    }
    private void RequestAPI(String line, String transport, String station){
        APIServices apiServices = new APIServices();
        String horaires = apiServices.getHorraireFromStation(line,transport,station);
        textView.setText(horaires);
    }
}
