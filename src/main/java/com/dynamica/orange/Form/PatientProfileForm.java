package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;

/**
 * Created by lordtemich on 1/15/18.
 */
public class PatientProfileForm {
        Patient patient;
        Client client;
        info info;
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
