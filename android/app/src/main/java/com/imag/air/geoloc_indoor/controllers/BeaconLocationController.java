package com.imag.air.geoloc_indoor.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.models.BeaconModel;
import com.imag.air.geoloc_indoor.services.NetworkService;
import com.imag.air.geoloc_indoor.services.RestService;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.services.AmazonService;
import com.imag.air.geoloc_indoor.services.UserLocationService;
import com.imag.air.geoloc_indoor.views.activities.MainActivity;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class BeaconLocationController {

    private AmazonService amazonService;
    private MapView mapView;
    private Context context;
    private List<BeaconModel> beacons;

    public BeaconLocationController(MapView mapView, Context context){
        amazonService = new AmazonService();
        beacons = new ArrayList<>();
        this.context = context;
        this.mapView = mapView;
    }

    public boolean subscribe(double beaconId){
        return false;
    }

    public boolean unsubscribe(double beaconId){
        return false;
    }

    public List<BeaconViewModel> getAvailableBeacons() {
        Log.i("GET_AVAILABLE_BEACON","getAvailableBeacon");
        // TODO : call Service + fill beacons List +convert BeaconModel to BeaconViewModel
        List<BeaconModel> beacons = amazonService.getAvailableBeacons();

        if(beacons == null){
            Log.i("beacons = null","beacons == null");
        }else{
            Log.i("beacons !=  null","beacons != null");
        }
        List<BeaconViewModel> beaconsVM = new ArrayList<>();
        for(BeaconModel b : beacons){
            Log.i("BEACON","BEACON : "+b.toString());
            beaconsVM.add(
                    new BeaconViewModel(
                            b.getBeaconId(),b.getLabel(),new GeoPoint(b.getLatitude(),b.getLongitude()),mapView,context));
        }

        return beaconsVM;
    }

    private BeaconModel getBeaconById(double beaconId){

       // TODO : get BeaconModel corresponding to Id and convert BeaconModel to BeaconViewModel
        return null;
    }

    public void enableInternetConnection(final Context context){

        // Check internet connection
        if (!NetworkService.networkChecking(context)) {
            Log.i("ENABLE_INTERNET_CONNECT","entering enableInternetConnection ");
            // Alert creation
            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            TextView alertTV = new TextView(context);
            alertTV.setText(R.string.need_internet);
            alertTV.setPadding(16, 0, 16, 0);
            alert.setTitle(R.string.no_internet_connection)
                    .setView(alertTV)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Enable wifi
                            Toast.makeText(context, "Please enable wifi or network to use the app", Toast.LENGTH_LONG).show();
                            NetworkService.setWifiEnabled(context, true);
                        }
                    }).create().show();
        }
    }
    /* Config ?

    public boolean addBeaconinDb(BeaconViewModel beaconViewModel){
        // Call service
        return false;
    }
    public boolean updateBeaconInDb(BeaconViewModel beaconViewModel){
        // Call service
        return false;
    }
    public boolean deleteBeaconInDb(double beaconId){
        // Call service
        return false;
    }

    */
}
