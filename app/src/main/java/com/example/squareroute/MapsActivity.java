package com.example.squareroute;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference reference;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        reference = FirebaseDatabase.getInstance().getReference("Universite");
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Universite universite = dataSnapshot.getValue(Universite.class);
                    LatLng coordonnees = new LatLng(universite.lat,universite.lng);
                    mMap.addMarker(new MarkerOptions().position(coordonnees).title(universite.nom_univ));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(MapsActivity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng paris = new LatLng(48.8534, 2.3488);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
    }
}