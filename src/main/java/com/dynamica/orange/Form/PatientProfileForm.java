package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;

import java.util.Date;

/**
 * Created by lordtemich on 1/15/18.
 */
public class PatientProfileForm {
    boolean myPatient=false;
        Patient patient;
        Client client;
        info info;
        boolean online;
        class info{
            String workC;
            String homeC;
            String blood;
            public info(){
            }
            public info(String workC, String homeC, String blood){
                this.workC=workC;
                this.homeC=homeC;
                this.blood=blood;
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

            public void setBlood(String blood) {
                this.blood = blood;
            }

            public void setHomeC(String homeC) {
                this.homeC = homeC;
            }

            public void setWorkC(String workC) {
                this.workC = workC;
            }
        }
        public PatientProfileForm(Patient patient, Client client, String workCity, String homeCity, String blood){
                this.patient=patient;
                this.client=client;
                info=new info(workCity,homeCity,blood);
            if((new Date().getTime())-client.getLastOnline()<=600000) {
                online=true;
            }
            else{
                online=false;
            }
        }

    public boolean isMyPatient() {
        return myPatient;
    }

    public void setMyPatient(boolean myPatient) {
        this.myPatient = myPatient;
    }

    public boolean isOnline() {
        return online;
    }

    public Client getClient() {
        return client;
    }

    public Patient getPatient() {
        return patient;
    }

    public PatientProfileForm.info getInfo() {
        return info;
    }
}
