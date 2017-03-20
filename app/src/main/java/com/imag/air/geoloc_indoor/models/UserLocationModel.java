package com.imag.air.geoloc_indoor.models;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationModel {

    private GeoPoint coordinates;
    // Context

    public UserLocationModel() {
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }
}
