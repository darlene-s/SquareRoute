package com.example.squareroute;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText connexionEmail,connexionPassword;
    TextView connexionInscription,connexionForgotPassword;
    Button connexionButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Ne pas oublier ça sinon c'est caca
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        connexionEmail = findViewById(R.id.editTextTextEmailAddress);
        connexionPassword = findViewById(R.id.editTextTextPassword);
        connexionButton = findViewById(R.id.connexion);
        connexionInscription = findViewById(R.id.inscription);
        connexionForgotPassword = findViewById(R.id.forgotpassword);

        connexionInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        connexionForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),dashboard.class)); // A changer
            }
        });


        connexionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = connexionEmail.getText().toString();
                String password = connexionPassword.getText().toString();

                if(email.isEmpty()){
                    connexionEmail.setError("Email manquant");
                    return;
                }

                if(password.isEmpty()){
                    connexionPassword.setError("Mot de passe manquant");
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(MainActivity.this, "Connexion réussie ! (enfin jcrois)", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), dashboard.class));
                        //jle renvoie sur la page Register mais normalement sa renvoit vers la page d'accueil c'est juste qu'elle existe pas encore du coup
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), dashboard.class));
        }
    }
}