package com.shopakolik.seniorproject.controller.model.shopakolikelements;

public class Location {
    private int locationId;
    private String location;
    private float latitude;
    private float longitude;
    private String address;

    public Location(String location, float latitude, float longitude, String address) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public Location(int locationId, String location, float latitude, float longitude, String address) {
        this.locationId = locationId;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
