package com.imag.air.geoloc_indoor.services;

import android.location.Location;
import android.os.Bundle;

import com.imag.air.geoloc_indoor.services.interfaces.IUserLocationService;

import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationService implements IUserLocationService {
    @Override
    public GeoPoint getLocation() {
        return null;
    }

    @Override
    public void disableGPS() {

    }

    @Override
    public void enableGPS() {

    }

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
