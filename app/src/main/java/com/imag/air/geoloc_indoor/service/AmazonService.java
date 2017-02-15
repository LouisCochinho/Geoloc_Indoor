package com.imag.air.geoloc_indoor.service;

import java.util.Arrays;
import java.util.List;

import com.imag.air.geoloc_indoor.domain.BeaconId;

/**
 * Created by Diana Stoian on 02.04.2016.
 */
public class AmazonService {

    public static final String rootUrl = "http://ec2-52-58-8-165.eu-central-1.compute.amazonaws.com:8080";


    public static List<BeaconId> getDeviceList(){
        String url = rootUrl + "/devices/listBeacons";
        BeaconId[] array = ARestTemplate.getForObject(url, BeaconId[].class);
        List<BeaconId> beaconsObj = Arrays.asList(array);
        return beaconsObj;
    }
}
