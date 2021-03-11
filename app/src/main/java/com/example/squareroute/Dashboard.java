package com.example.squareroute;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


public class Dashboard extends AppCompatActivity {
    Button logout, checkEmail,localisation;
    ImageButton activity6,activity2,activity3;
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
        activity2 = findViewById(R.id.btn_school);
        activity3 = findViewById(R.id.btn_library);
        localisation = findViewById(R.id.btn_localisation);


        if (!mauth.getCurrentUser().isEmailVerified()) {
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
                mauth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Dashboard.this, "Email de vérification envoyé", Toast.LENGTH_SHORT).show();
                        checkEmail.setVisibility(View.GONE);
                    }
                });
            }
        });

        if(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            localisation.setVisibility(View.GONE);
        }

        activity6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SquareInRealtime.class));
            }
        });
        activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getApplicationContext(), MapsActivityUniversity.class));
                else
                    Toast.makeText(Dashboard.this, "Veuillez autoriser la localisation", Toast.LENGTH_SHORT).show();
            }
        });
        activity3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    startActivity(new Intent(getApplicationContext(), MapsActivityBibliotheque.class));
                else
                    Toast.makeText(Dashboard.this, "Veuillez autoriser la localisation", Toast.LENGTH_SHORT).show();
            }
        });

        localisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(Dashboard.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                localisation.setVisibility(View.GONE);
                                Toast.makeText(Dashboard.this, "Permission granted", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                                    builder.setTitle("Permission Denied")
                                            .setMessage("Permission to access device location is permanently denied. you need to go to setting to allow the permission.")
                                            .setNegativeButton("Cancel", null)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                }
                                            })
                                            .show();
                                } else {
                                    Toast.makeText(Dashboard.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });


    }

    private void signOutUser (){
        Intent mainActivity = new Intent(Dashboard.this, MainActivity.class);
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Toast.makeText(Dashboard.this, "Déconnexion réussie !", Toast.LENGTH_SHORT).show();
        startActivity(mainActivity);
        Toast.makeText(Dashboard.this, "Déconnexion réussie !", Toast.LENGTH_SHORT).show();
        finish();
    }
}