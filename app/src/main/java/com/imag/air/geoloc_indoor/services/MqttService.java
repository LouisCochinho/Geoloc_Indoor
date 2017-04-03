package com.imag.air.geoloc_indoor.services;

import com.imag.air.geoloc_indoor.services.interfaces.IMqttService;

/**
 * Created by louis on 27/03/2017.
 */

public class MqttService implements IMqttService {
    @Override
    public boolean subscribe(double beaconId) {
        return false;
    }

    @Override
    public boolean unsubscribe(double beaconId) {
        return false;
    }
}
