package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lordtemich on 1/16/18.
 */
public class OrderListForm {
    String id;
    ArrayList<Service> services;
    ArrayList<OwnService> ownServices;
    long createdTime, choseTime;
    String status;
    double period;
    int calls, commings;
    boolean  atwork;
    CommentForm commentForms;
    DoctorListForm doctor;
    PatientListForm patient;
    ArrayList<EducationForm> educationForms;
    boolean online;
    public OrderListForm(){}

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderListForm(Order order, Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services, ArrayList<OwnService> ownServices, ArrayList<EducationForm> educationForms){
            id=order.getId();
            createdTime=order.getCreatedTime();
            choseTime=order.getChoseTime();
            status=order.getStatus();
            this.services=services;
            this.doctor=new DoctorListForm(doctor,client,serviceType,services);
            this.educationForms=educationForms;
            this.ownServices=ownServices;
        if(client.getLastOnline()-(new Date().getTime())<=600000){
            online=true;
        }
        else online=false;
    }
    public OrderListForm(Order order, Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services,ArrayList<OwnService> ownServices){
        id=order.getId();
        createdTime=order.getCreatedTime();
        choseTime=order.getChoseTime();
        status=order.getStatus();
        period=order.getPeriodinhours();
        this.services=services;
        this.doctor=new DoctorListForm(doctor,client,serviceType,services);
        this.ownServices=ownServices;
        if(client.getLastOnline()-(new Date().getTime())<=600000)
            online=true;
        else online=false;
    }
    public OrderListForm(Order order, Patient patient, Client client){
        id=order.getId();
        createdTime=order.getCreatedTime();
        choseTime=order.getChoseTime();
        status=order.getStatus();
        this.patient=new PatientListForm(patient,client);
        if(client.getLastOnline()-(new Date().getTime())<=600000)
            online=true;
        else online=false;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public CommentForm getCommentForms() {
        return commentForms;
    }

    public void setCommentForms(CommentForm commentForms) {
            this.commentForms = commentForms;
    }

    public void setAtwork(boolean atwork) {
        this.atwork = atwork;
    }

    public boolean isAtwork() {
        return atwork;
    }

    public int getCalls() {
        return calls;
    }

    public int getCommings() {
        return commings;
    }

    public double getPeriod() {
        return period;
    }

    public void setCalls(int calls) {
        this.calls = calls;
    }

    public void setCommings(int commings) {
        this.commings = commings;
    }

    public void setPeriod(double period) {
        this.period = period;
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

    public long getCreatedTime() {
        return createdTime;
    }

    public long getChoseTime() {
        return choseTime;
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

    public boolean isOnline() {
        return online;
    }
}
