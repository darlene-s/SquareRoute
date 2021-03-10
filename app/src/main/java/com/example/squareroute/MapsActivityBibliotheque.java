package com.example.squareroute;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.logo_bibli);
                    mMap.addMarker(new MarkerOptions().position(coordonnees).title(bibliotheque.nom_bibli).icon(icon));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(MapsActivityBibliotheque.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });
        LatLngBounds parisBounds = new LatLngBounds(
                new LatLng(-44, 113), // SW bounds
                new LatLng(-10, 154)  // NE bounds
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parisBounds.getCenter(), 10));
    }
}