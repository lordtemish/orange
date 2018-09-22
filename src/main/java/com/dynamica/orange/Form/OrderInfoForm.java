package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lordtemich on 1/17/18.
 */
public class OrderInfoForm {
    Order order;
    ClientWithDoctorForm doctor;
    ClientWithPatientForm patient;
    CommentForm commentForms;
    public OrderInfoForm(Order order, Doctor doctor, Client client, Patient patient, Client client1){
        this.order=order;
        this.doctor=new ClientWithDoctorForm(client,doctor);
        this.patient=new ClientWithPatientForm(client1,patient);
    }

    public void setCommentForms(CommentForm commentForms) {
        this.commentForms = commentForms;
    }

    public CommentForm getCommentForms() {
        return commentForms;
    }

    public Order getOrder() {
        return order;
    }

    public ClientWithDoctorForm getDoctor() {
        return doctor;
    }

    public ClientWithPatientForm getPatient() {
        return patient;
    }
    /*    public List<Object> getDoctor() {
        return doctor;
    }
    public List<Object> getPatient() {
        return patient;
    }*/

}
