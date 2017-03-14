package com.imag.air.geoloc_indoor.services;

import com.imag.air.geoloc_indoor.BeaconId;
import com.imag.air.geoloc_indoor.models.BeaconModel;
import com.imag.air.geoloc_indoor.services.interfaces.IAmazonService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Diana Stoian on 02.04.2016.
 */

// TODO PAHO
public class AmazonService implements IAmazonService{

    public static final String rootUrl = "http://ec2-52-58-8-165.eu-central-1.compute.amazonaws.com:8080";
    //public static final String rootUrl = "http://ec2-52-32-26-115.us-west-2.compute.amazonaws.com:8080";


    @Override
    public List<BeaconModel> getAvailableBeacons() {
        String url = rootUrl + "/devices/listBeacons";
        BeaconModel[] array = RestService.getForObject(url, BeaconModel[].class);
        List<BeaconModel> beaconsObj = Arrays.asList(array);
        return beaconsObj;
    }

    /*
    @Override
    public boolean addBeacon(BeaconModel beacon) {
        return false;
    }

    @Override
    public boolean updateBeacon(BeaconModel beacon) {
        return false;
    }

    @Override
    public boolean deleteBeacon(double beaconId) {
        return false;
    }

    @Override
    public BeaconModel getBeaconById(double beaconId) {
        return null;
    }
*/
    @Override
    public boolean subscribe(double beaconId) {
        // TODO
        return false;
    }

    @Override
    public boolean unsubscribe(double beaconId) {
        // TODO
        return false;
    }


}
