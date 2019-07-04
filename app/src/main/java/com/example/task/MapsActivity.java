package com.example.task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener {

    private GoogleMap mMap;

    private double longitude;
    private double latitude;

    private ImageButton currentLocation;
    private TextView location;

    private List<Services> servicesList = new ArrayList<>();
    private RecyclerView servicesRecyclerView;
    private RecyclerServicesAdapter servicesAdapter;


    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        googleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        currentLocation = (ImageButton) findViewById(R.id.current_location);
        location = (TextView) findViewById(R.id.location);

        currentLocation.setOnClickListener(this);

        servicesRecyclerView = findViewById(R.id.recyclerServices);

        servicesAdapter = new RecyclerServicesAdapter(servicesList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MapsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        servicesRecyclerView.setLayoutManager(horizontalLayoutManager);
        servicesRecyclerView.setAdapter(servicesAdapter);
        fillServices();
        setSearchBar();


    }
    private void setSearchBar() {
        Places.initialize(getApplicationContext(), "AIzaSyAiOzdaBotb1KgbVB_d-b1kFQduHPz7VO4");
        PlacesClient placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_place);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                location.setText(place.getId());
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                moveMap();

                Log.i("Hii this is ", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {

                Log.i("sorry bro", "An error occurred: " + status);
            }
        });

    }


    private void fillServices() {
        Services micro = new Services("Micro", R.drawable.cab);
        Services mini = new Services("Mini", R.drawable.cab);
        Services sedan = new Services("Sedan", R.drawable.cab);
        Services suv = new Services("SUV", R.drawable.cab);
        Services micro2 = new Services("Micro", R.drawable.cab);
        Services mini2 = new Services("Mini", R.drawable.cab);
        Services sedan2 = new Services("Sedan", R.drawable.cab);
        Services suv2 = new Services("SUV", R.drawable.cab);
        Services food = new Services("Food", R.drawable.food);
        servicesList.add(micro);
        servicesList.add(mini);
        servicesList.add(sedan);
        servicesList.add(suv);
        servicesList.add(micro2);
        servicesList.add(mini2);
        servicesList.add(sedan2);
        servicesList.add(suv2);
        servicesList.add(food);
        servicesAdapter.notifyDataSetChanged();

    }


    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(17.432, 78.407);
        mMap.addMarker(new MarkerOptions().position(sydney).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);

    }

    private void getCurrentLocation() {


        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    moveMap();
                }
            }
        });
    }

    private void moveMap() {
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title("currentLocation"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        mMap.animateCamera(CameraUpdateFactory.zoomTo(5));

        Toast.makeText(this, "Getting Current Location",Toast.LENGTH_LONG).show();
    }




    @Override
    public void onClick(View v) {
        if(v == currentLocation) {
            getCurrentLocation();
            moveMap();
            location.setText("Pinned Location");
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();

        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        moveMap();

    }
}
