package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Doctor {
    @Id
    private String id;
    private String client_id;
    private String position;
    private String info;
    private String homeadr;
    private String homecity;
    private Map homelocation;
    private String workadr;
    private String workcity;
    private Map worklocation;
    private String service_type_id;
    private ArrayList<Education> educations;
    private String serv_type;
    private ArrayList<Service> services;
    private ArrayList<Rate> rates;
    private ArrayList<OwnService> owns;
    private ArrayList<Experience> experiences;
    private ArrayList<String> profachievments;
    private ArrayList<String> extrainfo;
    private ArrayList<Comment> comments;
    public Doctor(){
        educations=new ArrayList<>();
        services=new ArrayList<>();
        rates=new ArrayList<>();
        owns=new ArrayList<>();
        experiences=new ArrayList<>();
        profachievments=new ArrayList<>();
        extrainfo=new ArrayList<>();
        comments=new ArrayList<>();
    }
    public Doctor(String client_id, String position, String info){
        this.client_id=client_id;
        this.position=position;
        this.info=info;
        educations=new ArrayList<>();
        services=new ArrayList<>();
        rates=new ArrayList<>();
        owns=new ArrayList<>();
        experiences=new ArrayList<>();
        profachievments=new ArrayList<>();
        extrainfo=new ArrayList<>();
        comments=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Map getHomelocation() {
        return homelocation;
    }

    public Map getWorklocation() {
        return worklocation;
    }

    public void setHomelocation(Map homelocation) {
        this.homelocation = homelocation;
    }

    public String getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(String service_type_id) {
        this.service_type_id = service_type_id;
    }

    public void setWorklocation(Map worklocation) {
        this.worklocation = worklocation;
    }

    public String getClient_id() {
        return client_id;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void addComment(Comment s){
        comments.add(s);
    }
    public boolean deleteComment(Comment s){
        if(comments.contains(s)){
            comments.remove(s);
            return true;
        }
        else
            return false;
    }
    public ArrayList<Education> getEducations() {
        return educations;
    }
    public void addEducation(Education s){
        educations.add(s);
    }
    public boolean deleteEducation(Education s){
        if(educations.contains(s)){
            educations.remove(s);
            return true;
        }
        else
            return false;
    }
    public boolean deleteExperience(Experience s){
        if(experiences.contains(s)){
            experiences.remove(s);
            return true;
        }
        else
            return false;
    }
    public ArrayList<Experience> getExperiences() {
        return experiences;
    }
    public void addExperience(Experience s){
        experiences.add(s);
    }

    public ArrayList<OwnService> getOwns() {
        return owns;
    }
    public void addOwnService(OwnService s){
        owns.add(s);
    }
    public boolean deleteOwnService(OwnService s){
        if(owns.contains(s)){
            owns.remove(s);
            return true;
        }
        else
            return false;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }
    public void addRate(Rate r){
        rates.add(r);
    }
    public boolean deleteRate(Rate s){
        if(rates.contains(s)){
            rates.remove(s);
            return true;
        }
        else
            return false;
    }
    public ArrayList<Service> getServices() {
        return services;
    }
    public boolean deleteService(Service s){
        if(services.contains(s)){
            services.remove(s);
            return true;
        }
        else
            return false;
    }
    public void addService(Service s){
        services.add(s);
    }

    public ArrayList<String> getExtrainfo() {
        return extrainfo;
    }
    public boolean deleteExtraInfo(String s){
        if(extrainfo.contains(s)){
            extrainfo.remove(s);
            return true;
        }
        else
            return false;
    }
    public void addExtraInfo(String s){
        extrainfo.add(s);
    }

    public ArrayList<String> getProfachievments() {
        return profachievments;
    }
    public void addProfAch(String s){
        profachievments.add(s);
    }
    public boolean deleteProfAchs(String s){
        if(profachievments.contains(s)){
            profachievments.remove(s);
            return true;
        }
        else
            return false;
    }

    public String getHomeadr() {
        return homeadr;
    }

    public String getHomecity() {
        return homecity;
    }

    public String getPosition() {
        return position;
    }

    public String getServ_type() {
        return serv_type;
    }

    public String getWorkadr() {
        return workadr;
    }

    public String getWorkcity() {
        return workcity;
    }

    public void setHomeadr(String homeadr) {
        this.homeadr = homeadr;
    }

    public void setHomecity(String homecity) {
        this.homecity = homecity;
    }

    public void setServ_type(String serv_type) {
        this.serv_type = serv_type;
    }

    public void setWorkadr(String workadr) {
        this.workadr = workadr;
    }

    public void setWorkcity(String workcity) {
        this.workcity = workcity;
    }
}
