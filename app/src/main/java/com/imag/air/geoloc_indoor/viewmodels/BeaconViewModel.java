package com.imag.air.geoloc_indoor.viewmodels;

import org.osmdroid.api.Marker;
import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public class BeaconViewModel {
    private double beaconId;
    private String label;
    private GeoPoint coordinates;
    private Marker marker;

    public BeaconViewModel(double beaconId, String label) {
        this.beaconId = beaconId;
        this.label = label;
    }

}
