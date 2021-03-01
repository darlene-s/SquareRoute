package com.example.squareroute;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
    EditText emailEditText;
    Button  resetPassword;
    ProgressBar progressBar;
    FirebaseAuth auth;

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
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }
    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();
        if(email.isEmpty()) {
            emailEditText.setError("L'email est nécéssaire pour réinitialiser le mot de passe");
            emailEditText.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Veuillez entrer une adresse mail valide");
            emailEditText.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               if (task.isSuccessful()) {
                   Toast.makeText(forgotpassword.this,"Un email de récupération vous a été envoyé",Toast.LENGTH_LONG).show();
                   progressBar.setVisibility(View.GONE);
                   startActivity(new Intent(getApplicationContext(),MainActivity.class));
               }else {
                   Toast.makeText(forgotpassword.this, "Veuillez réessayer à nouveau, votre mail est probablement invalide", Toast.LENGTH_LONG).show();

               }
            }
        });

        }
    }