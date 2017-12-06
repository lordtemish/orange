package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class OwnService {
    @Id
    private String id;
    private String name;
    private String own_time_type_id;
    private String info;
    private int price;
    public OwnService(){}
    public OwnService(String name, String own_time_type_id, String info, int price){
        this.name=name;this.own_time_type_id=own_time_type_id;this.info=info;this.price=price;
    }
    public String getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getInfo() {
        return info;
    }

    public String getName() {
        return name;
    }

    public String getOwn_time_type_id() {
        return own_time_type_id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwn_time_type_id(String own_time_type_id) {
        this.own_time_type_id = own_time_type_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
