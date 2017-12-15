package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Education {
    @Id
    private String id;
    private String ed_type_id;
    private String name;
    private String speciality;
    private String start_year;
    private String stop_year;
    private ArrayList<String> urls;
    public Education(){
        urls=new ArrayList<>();
    }
    public Education(String ed_type_id,String name, String speciality, String start_year, String stop_year){
        this.ed_type_id=ed_type_id;
        this.name=name;
        this.speciality=speciality;
        this.start_year=start_year;
        this.stop_year=stop_year;
        urls=new ArrayList<>();
    }
    public void addUrl(String url){urls.add(url);}
    public ArrayList<String> getUrls() {
        return urls;
    }
    public boolean deleteUrl(String url){
        if(urls.contains(url)){
            urls.remove(url);
            return true;
        }
        else{
            return false;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEd_type_id() {
        return ed_type_id;
    }

    public String getName() {
        return name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public String getStart_year() {
        return start_year;
    }

    public String getStop_year() {
        return stop_year;
    }

    public void setEd_type_id(String ed_type_id) {
        this.ed_type_id = ed_type_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public void setStop_year(String stop_year) {
        this.stop_year = stop_year;
    }
}
