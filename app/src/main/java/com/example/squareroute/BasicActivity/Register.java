package com.example.squareroute.BasicActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.squareroute.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe Register : Classe inscription, utilise Firebase Authenticator de la Firebase BOM
 * Une fois le formulaire d'inscription rempli, la classe utilisateur fait l'intermédiaire avec le code
 * Java et l'Utilisateur est enregistré dans la realtime database
 */

public class Register extends AppCompatActivity {
    EditText inscriptionEmail, inscriptionPrenom, inscriptionPassword;
    TextView inscriptionConnexion;
    Button inscriptionButton;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

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
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        inscriptionEmail = findViewById(R.id.editTextTextEmailAddress2);
        inscriptionPrenom = findViewById(R.id.editTextTextPersonName);
        inscriptionPassword = findViewById(R.id.editTextTextPassword2);
        inscriptionConnexion = findViewById(R.id.connexionredirect);
        inscriptionButton = findViewById(R.id.inscriptionbtn);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        inscriptionConnexion.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (TextView de redirection vers l'activité connexion)
             * et qui permet de lancer l'activité connexion si l'utilisateur possède déjà un compte
             *
             * @param v :  vue représentant la TextView de redirection vers l' activity Connexion lorsqu'elle est cliquée
             */
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (bouton de validation de l'inscription)
             * et qui permet de lancer l'activité Dashboard si les conditions relatives à l'inscription
             * d'un utilisateur sont respectées
             *
             * @param v :  vue représentant le bouton de validation de l'inscription lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                String email = inscriptionEmail.getText().toString();
                String prenom = inscriptionPrenom.getText().toString();
                String password = inscriptionPassword.getText().toString();

                //Condition vérifiant si l'adresse mail n'est pas vide
                if (email.isEmpty()) {
                    inscriptionEmail.setError("Email manquant");
                    return;
                }
                //Condition vérifiant si l'adresse mail est syntaxiquement correcte (@)
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inscriptionEmail.setError("Email invalide");
                    return;
                }
                //Condition vérifiant si le prénom n'est pas vide
                if (prenom.isEmpty()) {
                    inscriptionPrenom.setError("Prénom manquant");
                    return;
                }
                //Condition vérifiant si le mot de passe n'est pas vide
                if (password.isEmpty()) {
                    inscriptionPassword.setError("Mot de passe manquant");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    /**
                     * Fonction onComplete(), qui prend en paramètre une tâche qui est la combinaison d'un email à
                     * un mot de passe pour un utilisateur
                     *
                     * @param task : Tâche correspondant à la combinaison email/mot de passe utilisateur
                     */
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Si le formulaire d'inscription est correct, un nouvel utilisateur est instancié
                        if (task.isSuccessful()) {
                            User user = new User(email, prenom);

                            FirebaseDatabase.getInstance().getReference("Utilisateur")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                /**
                                 * Fonction onComplete(), qui prend en paramètre une tâche qui est la synchronisation avec la realtime database
                                 * et la sauvegarde du profil au sein de la BD
                                 *
                                 * @param task : Tâche correspondant à la synchronisation avec la realtime database et la sauvegarde du profil au
                                 *             sein de la BD
                                 */
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Nouvel utilisateur créé avec succès", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}