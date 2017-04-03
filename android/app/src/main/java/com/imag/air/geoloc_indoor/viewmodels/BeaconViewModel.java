package com.imag.air.geoloc_indoor.viewmodels;

import android.annotation.TargetApi;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import com.imag.air.geoloc_indoor.R;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.springframework.http.ContentCodingType;
import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class BeaconViewModel {

    private Context context;
    private double beaconId;
    private String label;
    private GeoPoint coordinates;
    private Marker marker;

    public BeaconViewModel(double beaconId, String label, GeoPoint coordinates, MapView mapView, Context context) {
        this.beaconId = beaconId;
        this.label = label;
        this.coordinates = coordinates;
        this.context = context;
        this.marker = new Marker(mapView);
        initMarker();
    }

    private void initMarker(){
        this.marker.setPosition(this.coordinates);
        this.marker.setTitle(label);
        this.marker.setSnippet(toString());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            setIcon19(this.marker);
        else
            setIcon21(this.marker);
    }

    @Override
    public String toString() {
        return  "ID : " + beaconId +
                " label : " + label  +
                " coordinates : {" + coordinates.getLatitude()+","+coordinates.getLongitude()+"}";
    }

    @TargetApi(19)
    private void setIcon19(Marker m) {
        m.setIcon(this.context.getResources().getDrawable(R.drawable.ic_place_red_600_48dp));
    }

    /**
     * Set an icon on a Marker (for API 21 and higher)
     *
     * @param m
     */
    @TargetApi(21)
    private void setIcon21(Marker m) {
        m.setIcon(this.context.getDrawable(R.drawable.ic_place_red_600_48dp));
    }

    public Marker getMarker() {
        return marker;
    }

    public double getBeaconId() {
        return beaconId;
    }

    public String getLabel() {
        return label;
    }
}
