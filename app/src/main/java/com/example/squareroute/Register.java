package com.example.squareroute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText inscriptionEmail,inscriptionPrenom,inscriptionPassword;
    TextView inscriptionConnexion;
    Button inscriptionButton;
    FirebaseAuth firebaseAuth;

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

        inscriptionConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        inscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inscriptionEmail.getText().toString();
                String prenom = inscriptionPrenom.getText().toString();
                String password = inscriptionPassword.getText().toString();

                if (email.isEmpty()) {
                    inscriptionEmail.setError("Email manquant");
                    return;
                }

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    inscriptionEmail.setError("Email invalide");
                    return;
                }

                //if (prenom.isEmpty()) {
                    //inscriptionPrenom.setError("Prénom manquant");
                    //return;
                //}

                if (password.isEmpty()) {
                    inscriptionPassword.setError("Mot de passe manquant");
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
                    @Override
                    public void onSuccess(AuthResult authResult ) {
                        startActivity(new Intent(getApplicationContext(),dashboard.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                       Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}