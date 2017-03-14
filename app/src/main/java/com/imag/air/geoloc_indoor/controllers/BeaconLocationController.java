package com.imag.air.geoloc_indoor.controllers;

import com.imag.air.geoloc_indoor.models.BeaconModel;
import com.imag.air.geoloc_indoor.viewmodels.BeaconViewModel;
import com.imag.air.geoloc_indoor.services.AmazonService;
import com.imag.air.geoloc_indoor.services.UserLocationService;

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
        // TODO : call Service
        // TODO : control
        return false;
    }

    public boolean unsubscribe(double beaconId){
        // TODO : call Service
        // TODO : control
        return false;
    }

    public List<BeaconViewModel> getAvailableBeacons() {
        // TODO : call Service + update beacons
        return null;
    }

    private BeaconModel getBeaconById(double beaconId){
       // TODO : call service
        return null;
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
