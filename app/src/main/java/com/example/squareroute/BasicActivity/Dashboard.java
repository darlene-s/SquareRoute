package com.example.squareroute.BasicActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.squareroute.Activity3.MapsActivityBibliotheque;
import com.example.squareroute.Activity2.MapsActivityUniversity;
import com.example.squareroute.R;
import com.example.squareroute.Activity4.SquareInRealtime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnSuccessListener;
/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe Dashboard : Classe Dashboard, Permet la redirection à
 * toutes les activités liées à des fonctionnalités prises en charge par SquareRoute
 */


public class Dashboard extends AppCompatActivity {
    Button logout, checkEmail;
    ImageButton activity6,activity2,activity3;
    FirebaseAuth mauth;

    @SuppressLint("WrongViewCast")
    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité Register
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité Register
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_dashboard);


        mauth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        checkEmail = findViewById(R.id.email_check);
        activity6 = findViewById(R.id.btn_squarerealtime);
        activity2 = findViewById(R.id.btn_school);
        activity3 = findViewById(R.id.btn_library);

        if(!mauth.getCurrentUser().isEmailVerified()){
            checkEmail.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (bouton de déconnexion)
             * et qui appelle les fonctions signOut() et signOutUser pour déconnecter l'utilisateur
             *
             * @param v :  vue représentant le bouton de déconnexion lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
              mauth.signOut();
              signOutUser();
            }
        });

        checkEmail.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (Bouton de vérification de l'email)
             * et qui permet d'envoyer un email de vérification du mail si ce dernier n'est pas vérfié
             * pour l'utilisateur courant
             *
             * @param v :  vue représentant le bouton de vérification de l'email lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                mauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>(){
                    /**
                     * Fonction onSuccess()
                     * Renvoie un toast qui permet de notifier l'utilisateur lorsque l'email de vérification est envoyé
                     * et de masquer le bouton de la view liée à l'activité
                     * @param aVoid : Paramètre vide
                     */
                    @Override
                    public void onSuccess(Void aVoid){
                        Toast.makeText(Dashboard.this,"Email de vérification envoyé", Toast.LENGTH_SHORT).show();
                        checkEmail.setVisibility(View.GONE);
                    }
                });
            }
        });
        activity6.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (bouton de redirection vers l'activité SquareInRealtime)
             * et qui permet de lancer l'activité SquareInRealtime
             *
             * @param v :  vue représentant le bouton de redirection vers  l'activité SquareInRealtime lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SquareInRealtime.class));
            }
        });
        activity2.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (bouton de redirection vers l'activité MapsActivityUniversity)
             * et qui permet de lancer l'activité MapsActivityUniversity
             *
             * @param v :    vue représentant le bouton de redirection vers l'activité MapsActivityUniversity lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivityUniversity.class));
            }
        });
        activity3.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (bouton de redirection vers l'activité MapsActivityBibliotheque)
             * et qui permet de lancer l'activité MapsActivityBibliotheque
             *
             * @param v :  vue représentant le bouton de redirection vers la MapsActivityBibliotheque lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivityBibliotheque.class));
            }
        });
    }

    /**
     * Fonction signOutUser()
     * Permet de déconnecter l'utilisateur courant et de le notifier de sa déconnexion
     */
    private void signOutUser() {
        Intent mainActivity = new Intent(Dashboard.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(Dashboard.this, "Déconnexion réussie !", Toast.LENGTH_SHORT).show();
        startActivity(mainActivity);
        Toast.makeText(Dashboard.this, "Déconnexion réussie !", Toast.LENGTH_SHORT).show();
        finish();


    }
}