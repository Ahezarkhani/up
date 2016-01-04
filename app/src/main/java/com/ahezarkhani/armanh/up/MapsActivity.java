package com.ahezarkhani.armanh.up;

import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int zoomFactor = 15;
    private PolylineOptions polyLineOption;
    private ArrayList<LatLng> latLngList;
    private ArrayList<Location> locationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        latLngList = Globals.runs.get(Globals.runToDisplay).runCoordinates;
        locationList = Globals.runs.get(Globals.runToDisplay).locationsList;

        LatLng LatLngElem;

        polyLineOption = new PolylineOptions();
        for(Location currLoc:locationList)
        {
            LatLngElem = new LatLng(currLoc.getLatitude(),currLoc.getLongitude());
            System.out.println(LatLngElem + "aaaaaaaaaaaaaaaaaa");
            polyLineOption.add(LatLngElem);
        }
        polyLineOption.visible(true);
        polyLineOption.geodesic(true);
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
    public void onMapReady(GoogleMap map) {


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationList.get(0).
                                getLatitude(),locationList.get(0).getLongitude()), zoomFactor));
        System.out.println(latLngList.size()+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        // Polylines are useful for marking paths and routes on the map.
        map.addPolyline(polyLineOption);


    }
}
