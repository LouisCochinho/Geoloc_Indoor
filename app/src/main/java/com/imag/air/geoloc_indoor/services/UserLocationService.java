package com.imag.air.geoloc_indoor.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.models.UserLocationModel;
import com.imag.air.geoloc_indoor.services.interfaces.IUserLocationService;

import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationService implements IUserLocationService {
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    @Override
    public void getLocation(UserLocationModel userLocation) {
        Context ctx = userLocation.getContext();
        LocationManager lm = userLocation.getLocationManager();
        try{
            if (!userLocation.isGPSEnabled() && !userLocation.isNetworkEnabled()) {
                // no network provider is enabled
            } else {
                userLocation.setCanGetLocation(true);

                if ( Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission( ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission( ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                }
                // First get location from Network Provider
                if (userLocation.isNetworkEnabled()) {
                    lm.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (lm != null) {
                        userLocation.setLocation(lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                        Location l = userLocation.getLocation();
                        if (l != null) {
                            userLocation.setCoordinates(new GeoPoint(l.getLatitude(),l.getLongitude()));
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (userLocation.isGPSEnabled()) {
                    if (userLocation.getLocation() == null) {
                        lm.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (lm != null) {
                            userLocation.setLocation(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                            Location l = userLocation.getLocation();
                            if (l != null) {
                                userLocation.setCoordinates(new GeoPoint(l.getLatitude(),l.getLongitude()));
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void disableGPS(UserLocationModel userLocation) {
        Context ctx = userLocation.getContext();
        LocationManager lm = userLocation.getLocationManager();
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( ctx, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( ctx, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }
        if(lm!= null){
            lm.removeUpdates(UserLocationService.this);
        }
    }

    @Override
    public void enableGPS(UserLocationModel userLocation) {
        final Context ctx = userLocation.getContext();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.enable_gps_dialog_title);

        // Setting Dialog Message
        alertDialog.setMessage(R.string.enable_gps_dialog_message);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    // TODO
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
