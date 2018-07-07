package com.dynamica.orange.Form;

public class PDAccept{
    boolean patient,doctor;
    public PDAccept(){
        patient=false;doctor=false;
    }

    public void setPatient(boolean patient) {
        this.patient = patient;
    }

    public void setDoctor(boolean doctor) {
        this.doctor = doctor;
    }

    public boolean isDoctor() {
        return doctor;
    }

    public boolean isPatient() {
        return patient;
    }
}