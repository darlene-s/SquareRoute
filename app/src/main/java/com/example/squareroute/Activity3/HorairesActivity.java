package com.example.squareroute.Activity3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.squareroute.R;

/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe HorairesActivity : Permet à l'utilisateur d'avoir accès aux horaires d'arrivée à sa station
 * d'une ligne de transport
 */
public class HorairesActivity extends AppCompatActivity {
    private TextView textView;

    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité HorairesActivity
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité HorairesActivity
     */
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

        RequestAPI(line, transport, station);


    }
    /**
     * Fonction requestAPI() qui prend en paramètre une ligne, une station et un mode de transport
     * en récupérant la ligne, la station et le mode de transport associé à travers une requête HTTP
     * faite dans la classe APIServices
     *
     * @param line      : String représentant une ligne de métro,bus,tram,RER
     * @param transport : String représentant le mode de transport (métro,bus,tram,RER)
     * @param station : String représentant une station de métro,bus,tram,RER
     */
    private void RequestAPI(String line, String transport, String station) {
        APIServices apiServices = new APIServices();
        String horaires = apiServices.getHorraireFromStation(line, transport, station);
        textView.setText(horaires);
    }
}
