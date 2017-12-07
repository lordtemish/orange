package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 12/6/17.
 */
public class Map {
    @Id
    private String id;
    private String location;
    public Map(){
    }
    public Map(String location){
        this.location=location;
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
