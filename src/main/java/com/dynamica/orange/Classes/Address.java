package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 1/20/18.
 */
public class Address {
    @Id
    String id;
    public Object cityid;
    public String address;
    public Map location;
    public String name;
    public Address(){}
    public Address(String cityid, String address){
        this.address=address;
        this.cityid=cityid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public Object getCityid() {
        return cityid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public void setLocation(Map location) {
        this.location = location;
    }
}

