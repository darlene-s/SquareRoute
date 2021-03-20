package com.example.squareroute.Activity3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.squareroute.R;

/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe SquareInRealTime : Dashboard redirigeant vers les LinesActivity
 * associées à chaque mode de transport disponible (Metro,Bus,Tram,RER)
 */
public class SquareInRealtime extends AppCompatActivity {

    ImageButton metro, rer, bus, tram;
    public static final String EXTRA_MESSAGE = "transport";

    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité SquareInRealTime
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité SquareInRealTime
     */
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
            /**
             * Fonction onClick() prenant en paramètre une vue (drawable associé au bouton métro)
             * et qui permet de lancer l'activité LinesActivity affichant les lignes de métro
             *
             * @param v : vue représentant le bouton metros lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE, new String("metros"));
                startActivity(intent);
            }
        });
        rer.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (drawable associé au bouton RER)
             * et qui permet de lancer l'activité LinesActivity affichant les lignes de RER
             *
             * @param v : vue représentant le bouton RER lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE, new String("rers"));
                startActivity(intent);
            }
        });
        bus.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (drawable associé au bouton bus)
             * et qui permet de lancer l'activité LinesActivity affichant les lignes de bus
             *
             * @param v : vue représentant le bouton bus lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE, new String("buses"));
                startActivity(intent);
            }
        });
        tram.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (drawable associé au bouton tram)
             * et qui permet de lancer l'activité LinesActivity affichant les lignes de tramway
             *
             * @param v : vue représentant le bouton tram lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getApplicationContext(),LinesActivity.class);
                intent.putExtra(EXTRA_MESSAGE,new String("tramways"));
                startActivity(intent);
            }
        });

    }

}