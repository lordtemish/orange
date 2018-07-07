package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lordtemich on 1/16/18.
 */
public class DoctorProfileForm {
    Doctor doctor;
    Client client;
    double rate;
    ServiceType serviceType;
    List<Service> services;
    City workCity;
    City homeCity;
    boolean online;
    int calls, commings;
    public DoctorProfileForm(Doctor doctor, Client client, ServiceType serviceType, List<Service> services, City workCity, City homeCity){
          this.doctor=doctor;
          this.client=client;
          this.serviceType=serviceType;
          this.services=services;
          this.workCity=workCity;
          this.homeCity=homeCity;
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
    public DoctorProfileForm(Doctor doctor, Client client, ServiceType serviceType, List<Service> services){
        this.doctor=doctor;
        this.client=client;
        this.serviceType=serviceType;
        this.services=services;
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

    public City getHomeCity() {
        return homeCity;
    }

    public City getWorkCity() {
        return workCity;
    }

    public boolean isOnline() {
        return online;
    }

    public double getRate() {
        return rate;
    }
}
