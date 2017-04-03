package com.imag.air.geoloc_indoor.services.interfaces;

/**
 * Created by louis on 27/03/2017.
 */

public interface IMqttService {
    // MQTT
    boolean subscribe(double beaconId);
    boolean unsubscribe(double beaconId);
}
