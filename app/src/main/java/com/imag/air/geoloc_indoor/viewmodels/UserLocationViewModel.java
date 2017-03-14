package com.imag.air.geoloc_indoor.viewmodels;

import android.content.Context;

import com.imag.air.geoloc_indoor.R;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationViewModel {

    private String label;
    private Context context;
    private GeoPoint coordinates;
    private Marker marker;

    public UserLocationViewModel(String label, GeoPoint coordinates, MapView mapView, Context context) {

        this.coordinates = coordinates;
        this.context = context;
        this.label = label;
        this.marker = new Marker(mapView);
        initMarker();
    }

    @Override
    public String toString() {
        return "UserLocationViewModel{" +
                "label='" + label + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }

    private void initMarker(){
        this.marker.setPosition(this.coordinates);
        this.marker.setTitle(label);
        this.marker.setSnippet(toString());
        this.marker.setIcon(this.context.getResources().getDrawable(R.drawable.ic_place_white_48dp));
    }

    public Marker getMarker() {
        return marker;
    }
}
