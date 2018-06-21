package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;

public class PatientListForm {
    String patientid;
    String name;
    String surname;
    Object photo;
    public PatientListForm(Patient patient, Client client){
        if(patient!=null){
            patientid=patient.getId();
        }
        else{
            patientid=null;
            patient=new Patient(null);
        }
        if(client!=null){
            name=client.getName();
            surname=client.getSurname();
            if(client.getPhotourl().size()>0){
               photo= client.getPhotourl().get(client.getPhotourl().size()-1);
            }
        }
        else{
            name=null;surname=null;
            client=new Client(null);
            client.setLang("R");
        }
    }

    public Object getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public String getPatientid() {
        return patientid;
    }

    public String getSurname() {
        return surname;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
