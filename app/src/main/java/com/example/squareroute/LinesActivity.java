package com.example.squareroute;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LinesActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> listLines;
    private ArrayAdapter<String> adapter = null;
    public static final String EXTRA_MESSAGE_LINE = "lines";
    public static final String EXTRA_MESSAGE_TRANSPORT = "transport";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_lines);

        listView = (ListView) findViewById(R.id.lignes);
        Intent intent = getIntent();
        final String transport =  intent.getStringExtra(squareinrealtime.EXTRA_MESSAGE);
        RequestAPI(transport);
        adapter = new ArrayAdapter<String>(LinesActivity.this,android.R.layout.simple_list_item_1,listLines);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ligne = (String) listView.getItemAtPosition(position);
                Intent intent2 = new Intent(LinesActivity.this,StationsActivity.class);
                intent2.putExtra(EXTRA_MESSAGE_LINE,ligne);
                intent2.putExtra(EXTRA_MESSAGE_TRANSPORT,transport);
                startActivity(intent2);
            }
        });




    }

    private void RequestAPI(String transport){
        APIServices apiServices = new APIServices();
        listLines=apiServices.getLinesFromTransport(transport);

    }
}
