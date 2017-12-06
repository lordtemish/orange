package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Blood {
    @Id
    private String id;
    private String name;
    public Blood(){
    }
    public Blood(String name){
        this.name=name;
    }
    public void setName(String name){this.name=name;}
    public String getName(){return name;}
    public String getId(){return id;}
}
