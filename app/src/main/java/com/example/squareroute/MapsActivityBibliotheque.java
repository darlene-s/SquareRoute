package com.example.squareroute;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

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

public class MapsActivityBibliotheque extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference reference;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_maps_bibliotheque);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        reference = FirebaseDatabase.getInstance().getReference("Bibliotheque");
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Bibliotheque bibliotheque = dataSnapshot.getValue(Bibliotheque.class);
                    LatLng coordonnees = new LatLng(bibliotheque.lat,bibliotheque.lng);
                    mMap.addMarker(new MarkerOptions().position(coordonnees).title(bibliotheque.nom_bibli));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(MapsActivityBibliotheque.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });
        // Add a marker in Sydney and move the camera
        LatLng paris = new LatLng(48.8534, 2.3488);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris));
    }
}