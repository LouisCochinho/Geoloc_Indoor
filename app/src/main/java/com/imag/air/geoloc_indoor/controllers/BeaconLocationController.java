package com.imag.air.geoloc_indoor.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.imag.air.geoloc_indoor.R;
import com.imag.air.geoloc_indoor.models.BeaconModel;
import com.imag.air.geoloc_indoor.services.NetworkService;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.services.AmazonService;
import com.imag.air.geoloc_indoor.services.UserLocationService;
import com.imag.air.geoloc_indoor.views.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louis on 13/03/2017.
 */

public class BeaconLocationController {

    private AmazonService amazonService;
    private List<BeaconModel> beacons;

    public BeaconLocationController(){
        amazonService = new AmazonService();
        beacons = new ArrayList<>();
    }

    public boolean subscribe(double beaconId){
        return false;
    }

    public boolean unsubscribe(double beaconId){
        return false;
    }

    public List<BeaconViewModel> getAvailableBeacons() {
        // TODO : call Service + fill beacons List +convert BeaconModel to BeaconViewModel
        return null;
    }

    private BeaconModel getBeaconById(double beaconId){

       // TODO : get BeaconModel corresponding to Id and convert BeaconModel to BeaconViewModel
        return null;
    }

    public void enableInternetConnection(final Context context){

        // Check internet connection
        if (!NetworkService.networkChecking(context)) {

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
