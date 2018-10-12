package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 12/6/17.
 */
public class Map {
    @Id
    private String id;
    private String location;
    double longitude, latitude;
    public Map(){
    }
    public Map(String location){
        this.location=location;
    }
    public Map(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public void setId(String id) {
        this.id = id;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
