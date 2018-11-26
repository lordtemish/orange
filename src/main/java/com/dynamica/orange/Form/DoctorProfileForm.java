package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lordtemich on 1/16/18.
 */
public class DoctorProfileForm {
    boolean myDoctor=false;
    boolean favouriteDoctor=false;
    boolean showPhones=false;
    List<CommentForm> commentForms=new ArrayList<>();
    Doctor doctor;
    Client client;
    double rate;
    ServiceType serviceType;
    List<Service> services;
    List<City> cities;
    boolean online;
    int calls, commings;

    public DoctorProfileForm(Doctor doctor, Client client, ServiceType serviceType, List<Service> services){
        this.doctor=doctor;
        this.client=client;
        this.serviceType=serviceType;
        this.services=services;
        cities=new ArrayList<>();
        if((new Date().getTime())-client.getLastOnline()<=600000) {
            online=true;
        }
        else{
            online=false;
        }
        double j=0;
        for(Rate i:doctor.getRates()){
            j+=i.getNum();
        }
        if(doctor.getRates().size()>0) {
            rate = j / doctor.getRates().size();
        }
        else{
            rate=-1;
        }
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCommentForms(List<CommentForm> commentForms) {
        this.commentForms = commentForms;
    }

    public List<CommentForm> getCommentForms() {
        return commentForms;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public boolean isFavouriteDoctor() {
        return favouriteDoctor;
    }

    public void setFavouriteDoctor(boolean favouriteDoctor) {
        this.favouriteDoctor = favouriteDoctor;
    }

    public boolean isMyDoctor() {
        return myDoctor;
    }

    public void setMyDoctor(boolean myDoctor) {
        this.myDoctor = myDoctor;
    }

    public boolean isShowPhones() {
        return showPhones;
    }

    public void setShowPhones(boolean showPhones) {
        this.showPhones = showPhones;
    }

    public int getCommings() {
        return commings;
    }

    public int getCalls() {
        return calls;
    }

    public void setCalls(int calls) {
        this.calls = calls;
    }

    public void setCommings(int commings) {
        this.commings = commings;
    }

    public Client getClient() {
        return client;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public List<Service> getServices() {
        return services;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }


    public boolean isOnline() {
        return online;
    }

    public double getRate() {
        return rate;
    }
}
