package com.imag.air.geoloc_indoor.models;

import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;

import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationModel {

    private GeoPoint coordinates;
    private Address address;

    public UserLocationModel() {
    }

    public UserLocationModel(GeoPoint coordinates, Address address) {
        this.coordinates = coordinates;
        this.address = address;
    }

    public GeoPoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoPoint coordinates) {
        this.coordinates = coordinates;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public double getLatitude(){
        return this.coordinates.getLatitude();
    }

    public double getLongitude(){
        return this.coordinates.getLongitude();
    }
}
