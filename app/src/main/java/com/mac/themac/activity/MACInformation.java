package com.mac.themac.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mac.themac.R;

/**
 * Created by Bryan on 10/5/2015.
 */
public class MACInformation extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_macinformation);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng mac = new LatLng(45.520430, -122.692424);
        map.addMarker(new MarkerOptions().position(mac).title("The MAC"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mac, 17.0f));
    }
}
