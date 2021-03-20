package com.example.squareroute.Activity4;

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
 * Classe LinesActivity :Permet à l'utilisateur de choisir une ligne associée à un moyen de transport
 * Metro,Bus ou Tramway parmi une liste de lignes disponibles grâce à une requête HTTP à l'API RATP.
 */
public class LinesActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> listLines;
    private ArrayAdapter<String> adapter = null;
    public static final String EXTRA_MESSAGE_LINE = "lines", EXTRA_MESSAGE_TRANSPORT = "transport";

    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité LinesActivity
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité LinesActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_lines);

        listView = (ListView) findViewById(R.id.lignes);
        Intent intent = getIntent();
        final String transport = intent.getStringExtra(SquareInRealtime.EXTRA_MESSAGE);
        RequestAPI(transport);
        adapter = new ArrayAdapter<String>(LinesActivity.this,android.R.layout.simple_list_item_1,listLines);
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
                String ligne = (String) listView.getItemAtPosition(position);
                Intent intent2 = new Intent(LinesActivity.this, StationsActivity.class);
                intent2.putExtra(EXTRA_MESSAGE_LINE, ligne);
                intent2.putExtra(EXTRA_MESSAGE_TRANSPORT, transport);
                startActivity(intent2);
            }
        });

    }

    /**
     * Fonction requestAPI() qui prend en paramètre  un mode de transport
     * en récupérant le mode de transport associé à travers une requête HTTP
     * faite dans la classe APIServices
     *
     * @param transport : String représentant le mode de transport (métro,bus,tram,RER)
     */
    private void RequestAPI(String transport) {
        APIServices apiServices = new APIServices();
        listLines = apiServices.getLinesFromTransport(transport);

    }
}
