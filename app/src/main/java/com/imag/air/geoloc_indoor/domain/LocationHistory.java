package com.imag.air.geoloc_indoor.domain;

/**
 * Created by Diana Stoian on 26.03.2016.
 */
public class LocationHistory {

    private Long id;

    private String label;

    private String date;

    private double latitude;

    private double longitude;

    private int level;

    public LocationHistory(long id, String label, double latitude, double longitude)
    {
        this.id = id;
        this.label = label;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
}
