package com.example.squareroute.BasicActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.squareroute.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe MainActivity : Classe Connexion, utilise Firebase Authenticator de la Firebase BOM
 * Une fois le formulaire de connexion rempli, la classe utilisateur fait l'intermédiaire avec le code
 * Java et authentifie l'utilisateur si celui-ci existe dans la BD
 */


public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    EditText connexionEmail, connexionPassword;
    TextView connexionInscription, connexionForgotPassword;
    Button connexionButton;
    FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private String userID;

    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité MainActivity
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité Register
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_main);

        //Firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        connexionEmail = findViewById(R.id.editTextTextEmailAddress);
        connexionPassword = findViewById(R.id.editTextTextPassword);
        connexionButton = findViewById(R.id.connexion);
        connexionInscription = findViewById(R.id.inscription);
        connexionForgotPassword = findViewById(R.id.forgotpassword);

        connexionInscription.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (TextView de redirection vers l'activité inscription)
             * et qui permet de lancer l'activité Register si l'utilisateur ne possède pas de compte SquareRoute
             *
             * @param v :  vue représentant la TextView de redirection vers l' activity Register lorsqu'elle est cliquée
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        connexionForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            /**
             * Fonction onClick() prenant en paramètre une vue (TextView de redirection vers l'activité ForgotPassword)
             * et qui permet de lancer l'activité  ForgotPassword si l'utilisateur a oublié son mot de passe
             *
             * @param v :  vue représentant la TextView de redirection vers l' activity ForgotPassword lorsqu'elle est cliquée
             */
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });


        connexionButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (bouton de validation de connexion)
             * et qui permet de lancer l'activité Dashboard si l'utilisateur est authentifié et qu'il souhaite
             * accèder aux services SquareRoute
             *
             * @param v :  vue représentant le bouton de validation de la connexion lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                String email = connexionEmail.getText().toString();
                String password = connexionPassword.getText().toString();

                //Condition vérifiant si l'adresse mail n'est pas vide
                if (email.isEmpty()) {
                    connexionEmail.setError("Email manquant");
                    return;
                }
                //Condition vérifiant si le mot de passe n'est pas vide
                if(password.isEmpty()) {
                    connexionPassword.setError("Mot de passe manquant");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password).addOnFailureListener(new OnFailureListener() {
                    /**
                     * Fonction onFailure()
                     *
                     * Lève une exception si l'utilisateur n'est pas correctement authentifié
                     *
                     * @param e : exception lié au non respect des conditions de la fonction onClick précédente
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Mot de passe ou email erroné veuillez réessayer" , Toast.LENGTH_SHORT).show();
                    }
                });
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    /**
                     * Fonction onSuccess()
                     * Appelée lorsque l'utilisateur s'est authentifié en respectant les conditions énoncées dans la fonction
                     * onClick()
                     * @param authResult : Résultat associé à l'authentification d'un utilisateur
                     */
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        reference = FirebaseDatabase.getInstance().getReference("Utilisateur");
                        userID = user.getUid();

                        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener(){
                            /**
                             * Fonction onDatachange()
                             * Récupère et retrouve les données de l'utilisateur en comparant les données utilisateur courantes avec celles contenues au sein de la BD
                             *
                             * @param snapshot Récupération des données contenues dans la BD pour comparaison
                             */
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot){
                                User user = snapshot.getValue(User.class);
                                if(user != null){
                                    String prenom = user.prenom;
                                    Toast.makeText(MainActivity.this, "Connexion réussie ! Bienvenue " + prenom + " !" , Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, " Votre email ou mot de passe est invalide", Toast.LENGTH_SHORT).show();
                                }
                            }
                            /**
                             * Fonction onCancelled(),
                             * Prend en charge les erreurs liées à une mauvaise synchronisation des données utilisateur courantes avec celles contenues au sein de la BD
                             * ou des erreurs de récupération de données au niveau de la BD
                             * @param error : Erreur liée à la récupération d'un utilisateur enregistré au sein de la BD
                             */
                            @Override
                            public void onCancelled(@NonNull DatabaseError error){
                                Toast.makeText(MainActivity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Fonction onStart()
     *
     * Récupère l'instance courante de l'utilisateur connecté et démarre l'activité Dashboard
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
        }
    }
}