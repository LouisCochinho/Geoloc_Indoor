package com.imag.air.geoloc_indoor.controllers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.imag.air.geoloc_indoor.models.UserLocationModel;
import com.imag.air.geoloc_indoor.services.UserLocationService;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;


import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationController {

    private UserLocationModel userLocation;
    private UserLocationService userLocationService;


    public UserLocationController(Context ctx){
        //userLocation  = new UserLocationModel();
        userLocationService = new UserLocationService(ctx);
    }

    public UserLocationViewModel getLocation(Context ctx, MapView mapView) throws Exception{
        Log.i("GET_USER_LOCATION","get user location (into controller).");
        /**
         * Call service
         */
        userLocation = userLocationService.getLocation();
        if(userLocation == null){
            Log.i("GET_USER_LOCATION","failed to get your location : userLocation == null ");
            throw new Exception("failed to get your location");
        }
        else{
            if(userLocation.getCoordinates() == null){
                Log.i("GET_USER_LOCATION","failed to get your location : coordinates == null ");
                throw new Exception("failed to get your location");
            }
            else if(userLocation.getAddress() == null){
                Log.i("GET_USER_LOCATION","failed to get address : address == null ");
                return new UserLocationViewModel("Me",userLocation.getCoordinates(),mapView,ctx);
            }
        }

        return new UserLocationViewModel("Me",
                userLocation.getCoordinates(),
                mapView,
                ctx,
                userLocation.getAddress().getAddressLine(0)
                        +" "
                        +userLocation.getAddress().getAddressLine(1)
                        +" "+userLocation.getAddress().getCountryName());

    }

    public UserLocationModel getUserLocationModel(){
        return this.userLocation;
    }
}
