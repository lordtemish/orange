package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

    /**
     * Created by lordtemich on 10/27/17.
     */
public class Patient {
    @Id
    private String id;
    private String clientid;
    private String gender;
    private int weight;
    private int height;
    private String blood;
    private String chronic;
    private String alergic;
    private String homeadr;
    private String homecity;
    private Map homelocation;
    private String workadr;
    private String workcity;
    private Map worklocation;
    private String date;
    private ArrayList<String> favs;
    private ArrayList<String> mydocs;
    public Patient(){
        favs=new ArrayList<>();
    }
    public Patient(String client_id){
        favs=new ArrayList<>();
        this.clientid=client_id;
        mydocs=new ArrayList<>();
    }
    public Patient(String client_id, String gender, int weight, int height, String chronic, String alergic){
        favs=new ArrayList<>();
        this.clientid=client_id;this.gender=gender;this.weight=weight;this.height=height;this.chronic=chronic;this.alergic=alergic;
        mydocs=new ArrayList<>();
    }
    public void setClientid(String client_id){this.clientid=client_id;}
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

        public void setHomelocation(Map homelocation) {
            this.homelocation = homelocation;
        }

        public void setWorklocation(Map worklocation) {
            this.worklocation = worklocation;
        }

        public void addFav(String id){favs.add(id);}
    public void addDoc(String id){mydocs.add(id);}
        public void setDate(String date) {
            this.date = date;
        }

        public String getDate() {
            return date;
        }

        public String getId(){return id;}
    public String getClientid(){return clientid;}
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

        public Map getWorklocation() {
            return worklocation;
        }

        public Map getHomelocation() {
            return homelocation;
        }

        public ArrayList<String> getFavs(){return favs;}
        public ArrayList<String> getMydocs() {
            return mydocs;
        }

        public boolean deleteFav(String id){
            if(favs.contains(id)){
                favs.remove(id);
                return true;
            }
            else return false;
        }
        public boolean deleteMyDoc(String id){
            if(mydocs.contains(id)){
                mydocs.remove(id);
                return true;
            }
            else return false;
        }
    }
