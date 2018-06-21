package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;

public class PatientClientForm {
    String clientid;
    String patientid;
    String name;
    String surname;
    String dadname;
    public PatientClientForm(Client client, Patient patient){
        name=client.getName();
        surname=client.getSurname();
        dadname=client.getDadname();
        clientid=client.getId();
        patientid=patient.getId();
    }

    public String getDadname() {
        return dadname;
    }

    public String getClientid() {
        return clientid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setDadname(String dadname) {
        this.dadname = dadname;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }
}
