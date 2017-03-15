package com.imag.air.geoloc_indoor.services.interfaces;

import android.location.Location;
import android.location.LocationListener;

import com.imag.air.geoloc_indoor.models.UserLocationModel;

import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public interface IUserLocationService extends LocationListener{
    void getLocation(UserLocationModel userLocation);
    void disableGPS(UserLocationModel userLocation);
    void enableGPS(UserLocationModel userLocation);
}
