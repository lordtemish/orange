
package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class OwnService {
    @Id
    private String id;
    private String name;
    private Object own_time_type_id;
    private String info;
    private int price, callPrice;
    private Boolean homeplace;
    public OwnService(){
        homeplace=false;
    }
    public OwnService(String name, int price, int callPrice){
        this.callPrice=callPrice;
        homeplace=true;
        this.name=name;
        this.price=price;
    }
    public OwnService(String name, String own_time_type_id, String info, int price, int call){
        this.name=name;this.own_time_type_id=own_time_type_id;this.info=info;this.price=price;callPrice=call;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void setCallPrice(int callPrice) {
        this.callPrice = callPrice;
    }

    public int getCallPrice() {
        return callPrice;
    }

    public Object getOwn_time_type_id() {
        return own_time_type_id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwn_time_type_id(Object own_time_type_id) {
        this.own_time_type_id = own_time_type_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Boolean getHomeplace() {
        return homeplace;
    }

    public void setHomeplace(Boolean homeplace) {
        this.homeplace = homeplace;
    }
}
