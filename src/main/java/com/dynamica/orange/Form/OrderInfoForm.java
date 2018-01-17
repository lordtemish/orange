package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Doctor;
import com.dynamica.orange.Classes.Order;
import com.dynamica.orange.Classes.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lordtemich on 1/17/18.
 */
public class OrderInfoForm {
    Order order;
    List<Object> doctor=new ArrayList<>();
    List<Object> patient=new ArrayList<>();

    public OrderInfoForm(Order order, Doctor doctor, Client client, Patient patient, Client client1){
        this.order=order;
        this.doctor.add(client);this.doctor.add(doctor);
        this.patient.add(client1);this.patient.add(patient);
    }
    public Order getOrder() {
        return order;
    }
    public List<Object> getDoctor() {
        return doctor;
    }

    public List<Object> getPatient() {
        return patient;
    }


}
