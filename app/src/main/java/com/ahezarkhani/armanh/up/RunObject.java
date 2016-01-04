package com.ahezarkhani.armanh.up;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by ArmanH on 12/6/2015.
 */
public class RunObject
{
    String date;
    String time;
    ArrayList<Integer> coachCues;
    ArrayList<LatLng> runCoordinates;
    ArrayList<Location> locationsList;
    ArrayList<Boolean> badFormOccurances;

    private final double METERTOMILE = 0.000621371;


    public RunObject(String d, String t)
    {
        date = d;

        String timeNum;
        String[] timeList = t.split(":");
        String hours = timeList[0]; // 004
        String minutes = timeList[1]; // 034556

        if(hours == "0")
            hours = "12";
        if(Integer.parseInt(minutes)<10)
            minutes = "0"+minutes;
        time = hours+":"+minutes;
        coachCues = new ArrayList<Integer>();
        runCoordinates= new ArrayList<LatLng>();
        locationsList = new ArrayList<Location>();
        badFormOccurances = new ArrayList<Boolean>();

    }

    public double getDistance()
    {
        double totalDistance = 0.0;
        Location lastLocation = locationsList.get(0); //gets initial location to compare
        Location currentLocation;
        for(int i = 1; i<locationsList.size();i++)
        {
            currentLocation = locationsList.get(i);
            totalDistance +=lastLocation.distanceTo(currentLocation);
            lastLocation = currentLocation;
        }
        totalDistance = totalDistance*METERTOMILE;  //CONVERTING METERS TO MILES
        return totalDistance;
    }
}
