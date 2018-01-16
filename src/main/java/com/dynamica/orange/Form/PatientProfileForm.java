package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;

/**
 * Created by lordtemich on 1/15/18.
 */
public class PatientProfileForm {
        Patient patient;
        Client client;
        String workC;
        String homeC;
        String blood;
        public PatientProfileForm(Patient patient, Client client, String workCity, String homeCity, String blood){
                this.patient=patient;
                this.client=client;
                this.workC=workCity;
                this.homeC=homeCity;
                this.blood=blood;
        }

    public Client getClient() {
        return client;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getBlood() {
        return blood;
    }

    public String getHomeC() {
        return homeC;
    }

    public String getWorkC() {
        return workC;
    }
}
