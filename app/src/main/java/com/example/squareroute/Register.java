package com.example.squareroute;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText inscriptionEmail,inscriptionPrenom,inscriptionPassword;
    TextView inscriptionConnexion;
    Button inscriptionButton;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

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

                if (prenom.isEmpty()) {
                    inscriptionPrenom.setError("Prénom manquant");
                    return;
                }

                if (password.isEmpty()) {
                    inscriptionPassword.setError("Mot de passe manquant");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task ) {
                        if(task.isSuccessful()){
                            User user = new User(email,prenom);

                            FirebaseDatabase.getInstance().getReference("Utilisateur")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task){
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Nouvel utilisateur créé avec succès", Toast.LENGTH_SHORT).show();
                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Register.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        }
                        else{
                            Toast.makeText(Register.this, "Erreur lors de l'inscription. Veuillez réessayer", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }
}