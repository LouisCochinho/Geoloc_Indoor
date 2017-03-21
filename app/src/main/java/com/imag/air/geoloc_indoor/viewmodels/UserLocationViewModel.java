package com.imag.air.geoloc_indoor.viewmodels;

import android.content.Context;
import android.renderscript.AllocationAdapter;
import android.util.Log;

import com.imag.air.geoloc_indoor.R;

import org.osmdroid.bonuspack.overlays.InfoWindow;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MarkerInfoWindow;
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
    private String address;

    public UserLocationViewModel(String label, GeoPoint coordinates, MapView mapView, Context context) {

        this.coordinates = coordinates;
        this.context = context;
        this.label = label;
        this.marker = new Marker(mapView);
        if(this.coordinates != null){
            initMarker();
        }
    }
    public UserLocationViewModel(String label, GeoPoint coordinates, MapView mapView, Context context, String address) {

        this.coordinates = coordinates;
        this.context = context;
        this.label = label;
        this.marker = new Marker(mapView);
        this.address = address;
        if(this.coordinates != null){
            initMarker();
        }
    }

    @Override
    public String toString() {
        if(this.address != null){
            return this.address;
        }
        else{
            return " coordinates : {" + coordinates.getLatitude()+","+coordinates.getLongitude()+"}";
        }
    }

    private void initMarker(){
        Log.i("INIT_MARKER","init marker with coordinates : "+this.coordinates.toString());
        this.marker.setPosition(this.coordinates);
        this.marker.setTitle(label);
        this.marker.setSnippet(toString());

        this.marker.setIcon(this.context.getResources().getDrawable(R.drawable.ic_place_white_48dp));
    }

    public Marker getMarker() {
        return marker;
    }
}
