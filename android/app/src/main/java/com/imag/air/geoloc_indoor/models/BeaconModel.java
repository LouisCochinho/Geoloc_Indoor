package com.imag.air.geoloc_indoor.models;

/**
 * Created by louis on 13/03/2017.
 */

public class BeaconModel {

    private double beaconId;
    private String label;
    private String date;
    private int level;
    private double latitude;
    private double longitude;
    private boolean isSubscribed;

    public BeaconModel(double beaconId, String label, String date, int level, boolean isSubscribed) {
        this.beaconId = beaconId;
        this.label = label;
        this.date = date;
        this.level = level;
        this.isSubscribed = isSubscribed;
    }



    // TODO : controls
    public double getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(double beaconId) {
        this.beaconId = beaconId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void subscribe(){
        this.isSubscribed = true;
    }
}
