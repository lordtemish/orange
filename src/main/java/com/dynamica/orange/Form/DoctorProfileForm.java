package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lordtemich on 1/16/18.
 */
public class DoctorProfileForm {
    Doctor doctor;
    Client client;
    ServiceType serviceType;
    List<Service> services;
    City workCity;
    City homeCity;
    public DoctorProfileForm(Doctor doctor, Client client, ServiceType serviceType, List<Service> services, City workCity, City homeCity){
          this.doctor=doctor;
          this.client=client;
          this.serviceType=serviceType;
          this.services=services;
          this.workCity=workCity;
          this.homeCity=homeCity;
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
}
