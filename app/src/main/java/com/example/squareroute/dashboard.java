package com.example.squareroute;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;


public class dashboard extends AppCompatActivity {
    Button logout, checkEmail, activity6;
    FirebaseAuth mauth;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_dashboard);


        mauth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        checkEmail = findViewById(R.id.email_check);
        activity6 = findViewById(R.id.btn_squarerealtime);

        if(!mauth.getCurrentUser().isEmailVerified()){
            checkEmail.setVisibility(View.VISIBLE);
        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              mauth.signOut();
              signOutUser();
            }
        });

        checkEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid){
                        Toast.makeText(dashboard.this,"Email de vérification envoyé", Toast.LENGTH_SHORT).show();
                        checkEmail.setVisibility(View.GONE);
                    }
                });
            }
        });
        activity6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),squareinrealtime.class));
            }
        });
    }
    private void signOutUser() {
        Intent mainActivity = new Intent(dashboard.this,MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainActivity);
        finish();


    }
}