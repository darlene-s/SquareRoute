package com.example.squareroute.BasicActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.squareroute.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe ForgotPassword : Permet à l'utilisateur de récupérer
 * son mot de passe en cas d'oubli
 *
 */
public class ForgotPassword extends AppCompatActivity {
    EditText emailEditText;
    Button  resetPassword;
    ProgressBar progressBar;
    FirebaseAuth auth;
    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité ForgotPassword
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité ForgotPassword
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_forgotpassword);

        emailEditText = findViewById(R.id.forgotpassword_email);
        resetPassword = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener() {
            /**
             * Fonction onClick() prenant en paramètre une vue (Bouton de rédéfinition du mot de passe)
             * et qui appelle la fonction resetPassword() pour rédéfinir le mot de passe
             *
             * @param v :  vue représentant le bouton de rédfinition du mot de passe lorsqu'il est cliqué
             */
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    /**
     * Fonction resetPassword()
     * Permet de vérifier si les conditions sont respectées pour changer le mot de passe
     * (formet de l'email)
     */
    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        // Condition permettant de verifier si l'email n'est pas vide
        if(email.isEmpty()) {
            emailEditText.setError("L'email est nécéssaire pour réinitialiser le mot de passe");
            emailEditText.requestFocus();
            return;
        }
        // Conditon permettant de vérifier si l'adresse mail est au bon format
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Veuillez entrer une adresse mail valide");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            /**
             * Fonction onComplete(), qui prend en paramètre une tâche qui est la récupération d'un
             * email pour l'envoi d'un lien de redéfinition du mot de passe
             *
             * @param task : Tâche correspondant à la récupération d'un email utilisateur pour
             *             l'envoi d'un mail de redéfinition du mot de passe
             */
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()) {
                   Toast.makeText(ForgotPassword.this,"Un email de récupération vous a été envoyé",Toast.LENGTH_LONG).show();
                   progressBar.setVisibility(View.GONE);
                   startActivity(new Intent(getApplicationContext(), MainActivity.class));
               }else {
                   Toast.makeText(ForgotPassword.this, "Veuillez réessayer à nouveau, votre mail est probablement invalide", Toast.LENGTH_LONG).show();

               }
            }
        });

        }
    }