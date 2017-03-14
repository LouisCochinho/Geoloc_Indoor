package com.imag.air.geoloc_indoor.viewmodels;

import org.osmdroid.api.Marker;
import org.osmdroid.util.GeoPoint;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationViewModel {

    private String label;
    private GeoPoint coordinates;
    private Marker marker;

    public UserLocationViewModel(String label) {
        this.label = label;
    }

}
