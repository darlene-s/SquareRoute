package com.example.squareroute.Activity1;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.squareroute.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import android.location.Location;

import android.view.View;


import com.google.android.gms.location.LocationCallback;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * @author G.Christian | S.Darlène | T.Kenny
 * Classe MapsActivityUniversity : Classe  MapsActivityUniversity , utilise l'API Google Maps
 * (Location service, direction service et affichage de la carte), cette activité permet à l'utilisateur
 * d'avoir un tracé depuis sa localisation/point de départ vers son point d'arrivée. Il y a également une liste
 * d'établissements universitaires épinglée à la carte.
 */

public class MapsActivityUniversity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseReference reference;
    private GoogleMap mMap;
    private static String TAG = "Info";

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private View mapView;
    private Button btnLocalisation, btnLocalisation2;

    private Marker markerSearch;

    private Marker markerLocalisation;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;


    private String apiKey = "AIzaSyAzsTP2GsxOFNjy-PUN0De5qWjn-0-Wvn4";

    // The entry point to the Places API.
    private PlacesClient placesClient;

    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    LatLng latLngLocalisation;



    private List<Polyline> polylines=null;
    private LatLng start;
    private LatLng destination;

    private Polyline currentPolyline;

    /**
     * Fonction onCreate() de base qui représente une étape du cycle de vie de l'activité
     * Initialisation de l'activité MapsActivityUniversity
     *
     * @param savedInstanceState : Variable permettant de sauvegarder l'état associé à l'instance courante de
     *                           l'activité MapsActivityUniversity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SquareRoute);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAzsTP2GsxOFNjy-PUN0De5qWjn-0-Wvn4");
            placesClient = Places.createClient(this);
        }

        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(48.646582, 1.868754),
                new LatLng(49.124015, 2.881794)
        ));

        autocompleteFragment.setCountries("FR");

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            /**
             * Fonction onPlaceSelected() qui ajoute un marker et adapte l'angle de vue de la map
             * en fonction du lieu choisi
             *
             * @param place : Lieu entré dans la barre de recherche
             */
            @Override
            public void onPlaceSelected(Place place) {
                if (!(markerSearch == null)) {
                    markerSearch.remove();
                    markerSearch = null;
                }
                Log.i(TAG, "Place" + place.getName() + ", " + place.getId());
                destination = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 16));
                markerSearch = mMap.addMarker(new MarkerOptions().position(destination).title(place.getName()));

                if(lastKnownLocation!=null){

                    start = (new LatLng(lastKnownLocation.getLatitude()
                            ,lastKnownLocation.getLongitude()));
                    traceRoute();
                }
            }

            /**
             * Fonction onError(), renvoie une erreur si un problème intervient au niveau du positionnement du marker où
             * du lieu indiqué au niveau de la barre de recherche
             *
             * @param status : Paramètre correspondant au statut de réalisation de la fonction onPlaceSelected()
             */
            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occured: " + status);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }



    /**
     * Fonction onMapReady()
     *
     * Manipule la carte une fois disponible, ce callback est déclenché lorsque la carte est prête à être utilisée.
     * La carte est initialisée avec les permissions de localisation, la géolocalisation de l'utilisateur, une vue centrée sur Paris
     * et son agglomération et des markers associés aux universités parisiennes répertoriées au sein de la realtime database
     *
     * @param googleMap : Fragment associé à la carte Google Map Universités
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        getLocationPermission();
        updateLocationUI();
        mMap = googleMap;


        reference = FirebaseDatabase.getInstance().getReference("Universite");
        reference.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Universite universite = dataSnapshot.getValue(Universite.class);
                    LatLng coordonnees = new LatLng(universite.lat,universite.lng);
                    BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.logo_univ);
                    mMap.addMarker(new MarkerOptions().position(coordonnees).title(universite.nom_univ).icon(icon));
                }
            }

            /**
             * Fonction onCancelled(), prend en paramètre une erreur si un problème de récupération des universités repertoriées
             * intervient
             *
             * @param error : Erreur liée à la récupération des données de l'entité Université au niveau de la BD
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error){
                Toast.makeText(MapsActivityUniversity.this, "Une erreur s'est produite ! Veuillez réessayer", Toast.LENGTH_SHORT).show();
            }
        });
        LatLngBounds parisBounds = new LatLngBounds(
                new LatLng(48.646582, 1.868754),
                new LatLng(49.124015, 2.881794)
        );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(parisBounds.getCenter(), 10));

        this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            /**
             * Fonction getInfoWindow()
             * Doit retourner "null" , pour que getInfoContents() soit appelé ensuite.
             *
             * @param arg0 : Booléen 0 si les fonctions précédentes sont correctement appelées, 1 si il y a une erreur
             * @return
             */
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            /**
             * Fonction getInfoContents()
             * Permet de gonfler les mises en page de la fenêtre d'information, du titre et de l'extrait.
             *
             * @param marker Marker contenant des informations sur les universités
             * @return
             */
            @Override
            public View getInfoContents(Marker marker) {
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });

        btnLocalisation = findViewById(R.id.btnLocalisation);
        btnLocalisation2 = findViewById(R.id.btnItineraire);

        btnLocalisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDeviceLocation();
                showCurrentPlace();
            }
        });

        btnLocalisation2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastKnownLocation!=null && destination !=null){
                    destination = (new LatLng(lastKnownLocation.getLatitude()
                            ,lastKnownLocation.getLongitude()));
                    traceRoute();
                }
                else{
                    Toast.makeText(MapsActivityUniversity.this,"Veuillez choisir une destination/indiquer votre position",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Fonction getLocationPermission()
     * <p>
     * Demande la permission de localisation, afin que nous puissions obtenir la localisation de l'appareil.
     * Le résultat de la demande de permission est traité par une callback onRequestPermissionsResult.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Fonction onRequestPermissionResult(), appelée lorsqu'une demande de géolocalisation est formulée par l'utilisateur,
     * elle vérifie si la demande est compatible avec les autorisations de géolocalisation accordées à l'application par l'appareil.
     *
     * @param requestCode  : Array d'Int associée au code de requête passé dans ActivityCompat.requestPermissions(android.app.Activity, String[], int)
     * @param permissions  : Array de String correspondant aux permissions demandées.
     * @param grantResults : Array d'Int associée  résultats de l'octroi des permissions correspondantes qui sont soit PackageManagerPERMISSION_GRANTED soit
     *                     PackageManager.PERMISSION_DENIED.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * Fonction updateLocationUI()
     * <p>
     * Permet de mettre à jour la localisation de l'utilisateur automatiquement
     * en fonction des autorisations accordées par l'appareil à SquareRoute
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Fonction getDeviceLocation()
     * <p>
     * Permet d'obtenir la meilleure et plus récente localisation de l'appareil,
     * qui peut être nulle dans de rares cas où une localisation n'est pas disponible.
     */
    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {

                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            start = (new LatLng(lastKnownLocation.getLatitude()
                                    ,lastKnownLocation.getLongitude()));
                            if (lastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), 16));
                                mMap.setMyLocationEnabled(true);
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(new LatLng(48.646582, 1.868754),10));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Fonction onOptionsItemSelected
     * Permet d'appeler la fonction showCurrentPlace si le bouton localisation
     * est cliqué et que la localisation est activée
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.btnLocalisation) {
            showCurrentPlace();
        }
        return true;
    }

    /**
     * Fonction showCurrentPlace()
     * Renvoie à l'utilisateur des lieux d'intérêt à proximité de lui pour en déduire sa localisation
     */
    private void showCurrentPlace() {
        if (mMap == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        MapsActivityUniversity.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");


            getLocationPermission();
        }
    }

    /**
     * Fonction openPlacesDialog
     * Ouvre une fenêtre de dialogue avec les lieux à proximité de la localisation actuelle de l'utilisateur
     */
    private void openPlacesDialog() {
        Toast.makeText(MapsActivityUniversity.this,"Choissisez l'endroit où vous vous situez s'il est présent dans la liste",Toast.LENGTH_LONG).show();
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                if (!(markerLocalisation == null)) {
                    markerLocalisation.remove();
                    markerLocalisation = null;
                }

                markerLocalisation = mMap.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        16));
                Location temp = new Location(LocationManager.GPS_PROVIDER);
                temp.setLatitude(markerLatLng.latitude);
                temp.setLongitude(markerLatLng.longitude);
                lastKnownLocation = new Location(temp);
                start = (new LatLng(lastKnownLocation.getLatitude()
                        ,lastKnownLocation.getLongitude()));
            }
        };

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Localisation")
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    /**
     * Fonction getUrl
     * Récupère l'url nécessaire pour l'api Google Maps Directions
     */
    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    /**
     * Fonction traceRoute
     * Trace la route entre un endroit de départ et un endroit d'arrivée sur la map
     */
    public void traceRoute() {
        //Execute Directions API request
        if (start != null && destination != null) {

            if (!(markerLocalisation == null)) {
                markerLocalisation.remove();
                markerLocalisation = null;
            }
            if (!(markerSearch == null)) {
                markerSearch.remove();
                markerSearch = null;
            }
            markerLocalisation = mMap.addMarker(new MarkerOptions().position(start).title("Position actuelle"));
            markerSearch = mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));


            List<LatLng> path = new ArrayList();
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(apiKey)
                    .build();
            String startString = start.latitude + "," + start.longitude;
            String destinationString = destination.latitude + "," + destination.longitude;
            DirectionsApiRequest req = DirectionsApi.getDirections(context, startString, destinationString);
            try {
                DirectionsResult res = req.await();

                //Loop through legs and steps to get encoded polylines of each step
                if (res.routes != null && res.routes.length > 0) {
                    DirectionsRoute route = res.routes[0];

                    if (route.legs != null) {
                        for (int i = 0; i < route.legs.length; i++) {
                            DirectionsLeg leg = route.legs[i];
                            if (leg.steps != null) {
                                for (int j = 0; j < leg.steps.length; j++) {
                                    DirectionsStep step = leg.steps[j];
                                    if (step.steps != null && step.steps.length > 0) {
                                        for (int k = 0; k < step.steps.length; k++) {
                                            DirectionsStep step1 = step.steps[k];
                                            EncodedPolyline points1 = step1.polyline;
                                            if (points1 != null) {
                                                //Decode polyline and add points to list of route coordinates
                                                List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                                for (com.google.maps.model.LatLng coord1 : coords1) {
                                                    path.add(new LatLng(coord1.lat, coord1.lng));
                                                }
                                            }
                                        }
                                    } else {
                                        EncodedPolyline points = step.polyline;
                                        if (points != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords = points.decodePath();
                                            for (com.google.maps.model.LatLng coord : coords) {
                                                path.add(new LatLng(coord.lat, coord.lng));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.getLocalizedMessage());
            }

            //Draw the polyline
            if (path.size() > 0) {
                if(currentPolyline != null){
                    currentPolyline.remove();
                }
                PolylineOptions opts = new PolylineOptions().addAll(path).color(R.color.main_green).width(5);
                currentPolyline = mMap.addPolyline(opts);
            }

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 11));
        }
        else{
            Toast.makeText(MapsActivityUniversity.this,"Erreur lors du tracage de l'itinéraire",Toast.LENGTH_LONG).show();
        }
    }


}