package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Patient {
    @Id
    private String id;
    private String client_id;
    private String gender;
    private int weight;
    private int height;
    private String blood;
    private String chronic;
    private String alergic;
    private String homeadr;
    private String homecity;
    private String workadr;
    private String workcity;
    private ArrayList<String> favs;
    public Patient(){
        favs=new ArrayList<>();
    }
    public Patient(String client_id){
        favs=new ArrayList<>();
        this.client_id=client_id;
    }
    public Patient(String client_id, String gender, int weight, int height, String chronic, String alergic){
        favs=new ArrayList<>();
        this.client_id=client_id;this.gender=gender;this.weight=weight;this.height=height;this.chronic=chronic;this.alergic=alergic;
    }
    public void setClient_id(String client_id){this.client_id=client_id;}
    public void setGender(String gender){this.gender=gender;}
    public void setWeight(int weight){this.weight=weight;}
    public void setHeight(int height){this.height=height;}
    public void setBlood(String id){this.blood=id;}
    public void setChronic(String s){this.chronic=s;}
    public void setAlergic(String s){this.alergic=s;}
    public void setHomeadr(String homeadr){this.homeadr=homeadr;}
    public void setWorkadr(String homeadr){this.workadr=homeadr;}
    public void setHomecity(String s){this.homecity=s;}
    public void setWorkcity(String s){this.workcity=s;}
    public void addFav(String id){favs.add(id);}

    public String getId(){return id;}
    public String getClient_id(){return client_id;}
    public String getGender(){return gender;}
    public int getWeight(){return weight;}
    public int getHeight(){return height;}
    public String getBlood(){return blood;}
    public String getChronic(){return  chronic;}
    public String getAlergic(){return alergic;}
    public String getHomeadr(){return homeadr;}
    public String getHomecity(){return homecity;}
    public String getWorkadr(){return workadr;}
    public String getWorkcity(){return workcity;}
    public ArrayList<String> getFavs(){return favs;}
}
