package com.example.squareroute.Activity3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.squareroute.R;

import java.util.List;
/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe StationActivity :Permet à l'utilisateur de choisir une station associée à une ligne de RER,
 * Metro,Bus ou Tramway parmi une liste de stations disponibles grâce à une requête HTTP à l'API RATP.
 */
public class StationsActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> listStations;
    private ArrayAdapter<String> adapter;
    public static final String EXTRA_MESSAGE_LINE = "lines", EXTRA_MESSAGE_TRANSPORT = "transport",
            EXTRA_MESSAGE_STATION = "station";

    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité StationActivity
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité StationsActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_stations);
        listView = (ListView) findViewById(R.id.stations);

        Intent intent = getIntent();
        final String line = intent.getStringExtra(LinesActivity.EXTRA_MESSAGE_LINE);
        final String transport = intent.getStringExtra(LinesActivity.EXTRA_MESSAGE_TRANSPORT);
        System.out.println("LINE : " + line + " TRANSPORT : " + transport);

        requestAPI(line, transport);
        System.out.println("SIZE : " + listStations.size());
        adapter = new ArrayAdapter<String>(StationsActivity.this, android.R.layout.simple_list_item_1, listStations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Fonction onItemClick() qui prend en charge l'adaptateur qui met en lien
             * la listView et les données qu'elle contient au click
             *
             * @param parent   : L'AdapterView sur lequel le click s'est produit
             * @param view     : La vue de l'AdapterView qui a été cliquée (vue fournie par l'adapter)
             * @param position : La position de la vue dans l'adapter
             * @param id       : L'identifiant de ligne de l'élément sur lequel on a cliqué
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String station = (String) listView.getItemAtPosition(position);
                Intent intent1 = new Intent(StationsActivity.this, HorairesActivity.class);
                intent1.putExtra(EXTRA_MESSAGE_LINE, line);
                intent1.putExtra(EXTRA_MESSAGE_TRANSPORT, transport);
                intent1.putExtra(EXTRA_MESSAGE_STATION, station);

                startActivity(intent1);
            }
        });
    }

    /**
     * Fonction requestAPI() qui prend en paramètre une ligne et un mode de transport
     * en récupérant la ligne et le mode de transport associé à travers une requête HTTP
     * faite dans la classe APIServices
     *
     * @param line      : String représentant une ligne de métro,bus,tram,RER
     * @param transport : String représentant le mode de transport (métro,bus,tram,RER)
     */
    private void requestAPI(String line, String transport) {
        APIServices apiServices = new APIServices();
        listStations = apiServices.GetStationsFromLine(line, transport);

    }
}
