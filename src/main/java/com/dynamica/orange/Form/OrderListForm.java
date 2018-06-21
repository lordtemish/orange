package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lordtemich on 1/16/18.
 */
public class OrderListForm {
    String id;
    ArrayList<Service> services;
    ArrayList<OwnService> ownServices;
    long time;
    String status;
    DoctorListForm doctor;
    PatientListForm patient;
    ArrayList<EducationForm> educationForms;
    public OrderListForm(Order order, Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services,ArrayList<OwnService> ownServices, ArrayList<EducationForm> educationForms){
            id=order.getId();
            time=order.getCreatedTime();
            status=order.getStatus();
            this.services=services;
            this.doctor=new DoctorListForm(doctor,client,serviceType,services);
            this.educationForms=educationForms;
            this.ownServices=ownServices;
    }
    public OrderListForm(Order order, Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services,ArrayList<OwnService> ownServices){
        id=order.getId();
        time=order.getCreatedTime();
        status=order.getStatus();
        this.services=services;
        this.doctor=new DoctorListForm(doctor,client,serviceType,services);
        this.ownServices=ownServices;
    }
    public OrderListForm(Order order, Patient patient, Client client){
        id=order.getId();
        time=order.getCreatedTime();
        status=order.getStatus();
        this.patient=new PatientListForm(patient,client);
    }

    public PatientListForm getPatient() {
        return patient;
    }

    public void setPatient(PatientListForm patient) {
        this.patient = patient;
    }

    public ArrayList<EducationForm> getEducationForms() {
        return educationForms;
    }

    public long getTime() {
        return time;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public DoctorListForm getDoctor() {
        return doctor;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<OwnService> getOwnServices() {
        return ownServices;
    }

    public void setOwnServices(ArrayList<OwnService> ownServices) {
        this.ownServices = ownServices;
    }
}
