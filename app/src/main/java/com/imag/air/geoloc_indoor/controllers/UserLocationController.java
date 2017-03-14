package com.imag.air.geoloc_indoor.controllers;

import com.imag.air.geoloc_indoor.models.UserLocationModel;
import com.imag.air.geoloc_indoor.services.UserLocationService;
import com.imag.air.geoloc_indoor.viewmodels.UserLocationViewModel;

/**
 * Created by louis on 13/03/2017.
 */

public class UserLocationController {

    private UserLocationModel userLocation;
    private UserLocationService userLocationService;

    public UserLocationController(){
        userLocationService = new UserLocationService();
    }

    public UserLocationViewModel getLocation(){
        // TODO : control + call service
        return null;
    }


}
