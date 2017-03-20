package com.imag.air.geoloc_indoor.controllers;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.imag.air.geoloc_indoor.models.UserLocationModel;
import com.imag.air.geoloc_indoor.services.UserLocationService;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationController {

    private UserLocationModel userLocation;
    private UserLocationService userLocationService;

    public UserLocationController(Context ctx){
        userLocation  = new UserLocationModel();
        userLocationService = new UserLocationService(ctx);
    }

    public UserLocationViewModel getLocation(Context ctx, MapView mapView){
        Log.i("GET_USER_LOCATION","get user location (into controller).");

        Location l = userLocationService.getLocation();
        if(l == null){
            Log.i("GET_USER_LOCATION","failed to get your location : location == null : ");
            Toast.makeText(ctx,"failed to get your location",Toast.LENGTH_SHORT).show();
            return null;
        }
        else{
            this.userLocation.setCoordinates(new GeoPoint(l.getLatitude(),l.getLongitude()));
            if(userLocation.getCoordinates()==null){
                Log.i("GET_USER_LOCATION","failed to get your location coordinates == null : ");
                Toast.makeText(ctx,"failed to get your location",Toast.LENGTH_SHORT).show();
                return null;
            }
            else{
                Log.i("GET_USER_LOCATION","get user location success : "+userLocation.getCoordinates().toString());
                return new UserLocationViewModel("Your location",userLocation.getCoordinates(),mapView,ctx);
            }
        }
    }

    public UserLocationModel getUserLocationModel(){
        return this.userLocation;
    }
}
