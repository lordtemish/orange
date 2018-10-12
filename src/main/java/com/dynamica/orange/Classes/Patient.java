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
    private Address homeAddress;
    private Address workAddress;
    private ArrayList<Address> addresses;
    private String date;
    private ArrayList<String> favs;
    private ArrayList<String> mydocs;
    public Patient(){
        favs=new ArrayList<>();mydocs=new ArrayList<>();addresses=new ArrayList<>();
    }
    public Patient(String client_id){
        favs=new ArrayList<>();
        this.clientid=client_id;
        mydocs=new ArrayList<>();
        addresses=new ArrayList<>();
    }
    public Patient(String client_id, String gender, int weight, int height, String chronic, String alergic){
        favs=new ArrayList<>();
        this.clientid=client_id;this.gender=gender;this.weight=weight;this.height=height;this.chronic=chronic;this.alergic=alergic;
        mydocs=new ArrayList<>();
        addresses=new ArrayList<>();
    }
    public void setClientid(String client_id){this.clientid=client_id;}
    public void setGender(String gender){this.gender=gender;}
    public void setWeight(int weight){this.weight=weight;}
    public void setHeight(int height){this.height=height;}
    public void setBlood(String id){this.blood=id;}
    public void setChronic(String s){this.chronic=s;}
    public void setAlergic(String s){this.alergic=s;}

        public ArrayList<Address> getAddresses() {
            return addresses;
        }
        public void addAddress(Address s){
            String id=s.getId();
            try {
                if (id.isEmpty()) {
                    id = System.identityHashCode(s) + "";
                }
            }
            catch (NullPointerException e){
                id = System.identityHashCode(s) + "";
            }
            s.setId(id);
            addresses.add(s);
        }
        public void clearAddresses(){
            addresses.clear();
        }
        public boolean deleteAddressById(String id){
            for(Address i : addresses){
                if(i.getId().equals(id)){
                    addresses.remove(i);
                    return true;
                }
            }
            return false;
        }
        public boolean changeAddressById(String id, Address ad){
            for(Address i : addresses){
                if(i.getId().equals(id)){
                    int ind=addresses.indexOf(i);
                    ad.setId(i.getId());
                    addresses.set(ind,ad);
                    return true;
                }
            }
            return false;
        }
        public void setHomeAddress(Address s) {
            String id=s.getId();
            try {
                if (id.isEmpty()) {
                    id = System.identityHashCode(s) + "";
                }
            }
            catch (NullPointerException e){
                id = System.identityHashCode(s) + "";
            }
            s.setId(id);
            this.homeAddress = s;
        }

        public void setWorkAddress(Address s) {
            String id=s.getId();
            try {
                if (id.isEmpty()) {
                    id = System.identityHashCode(s) + "";
                }
            }
            catch (NullPointerException e){
                id = System.identityHashCode(s) + "";
            }
            s.setId(id);
            this.workAddress = s;
        }
        public void deleteWorkAddress(){
            this.workAddress=null;
        }
        public void deleteHomeAddress(){
            this.homeAddress=null;
        }
        public Address getHomeAddress() {
            return homeAddress;
        }

        public Address getWorkAddress() {
            return workAddress;
        }

        public void addFav(String id){favs.add(id);}
    public boolean addDoc(String id){
        if(mydocs.contains(id)){
            return false;
        }
        else {
            mydocs.add(id);
            return true;
        }
    }
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


        public ArrayList<IDObject> getFavs(){
        ArrayList<IDObject> idObjects=new ArrayList<>();
            for(String i:favs){
                idObjects.add(new IDObject(i));
            }
            return idObjects;}

            public boolean favContains(String id){
        return  favs.contains(id);
            }
        public ArrayList<IDObject> getMydocs() {
            ArrayList<IDObject> idObjects=new ArrayList<>();
            for(String i:mydocs){
                idObjects.add(new IDObject(i));
            }
            return idObjects;
        }
        public ArrayList<String> getMyDocs(){
            return mydocs;
        }
        public boolean docContains(String id){
            return mydocs.contains(id);
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
