package com.example.luckoftheirish;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMapsAPIListner{

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int DEFAULT_ZOOM = 15;
    private GoogleMapsAPIManager apiManager;
    private ArrayList<IrishPub> irishPubs;
    private ImageView menuButton;
    public  LuckOfTheIrishAdapter adapter;
    public  RecyclerView recyclerView;


    private FragmentManager fragmentManager;


    private PubListFragment pubListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        fragmentManager = getSupportFragmentManager();
        pubListFragment = new PubListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, pubListFragment);
        ft.hide(pubListFragment);
        ft.commit();

        irishPubs = new ArrayList<>();
        apiManager = new GoogleMapsAPIManager(getApplicationContext(), this);
        apiManager.getPubs();


        menuButton = findViewById(R.id.button_menu);

        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openCloseMenu();
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0
                    );
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        } else {
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        }



    }

    public void getDeviceLocation(){

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0
                );
                getDeviceLocation();
            }else{
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            Location location = task.getResult();
                            LatLng currentPos = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(currentPos, DEFAULT_ZOOM);
                            mMap.moveCamera(update);
                        }
                    }
                });
            }
    }


    public void openCloseMenu(){

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        adapter = new LuckOfTheIrishAdapter(irishPubs);
        recyclerView.setAdapter(adapter);

        if(pubListFragment.isHidden()){
            ft.show(pubListFragment);
        }else{
            ft.hide(pubListFragment);
        }
        ft.commit();
      }


    @Override
    public void onIrishPubsAvailable(IrishPub irishPub) {
        irishPubs.add(irishPub);

        mMap.addMarker(new MarkerOptions()
        .position(irishPub.latLng)
        .title(irishPub.name)
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));


    }



}
