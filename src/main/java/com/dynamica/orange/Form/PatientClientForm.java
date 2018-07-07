package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;

public class PatientClientForm {
    String patientid;

    public PatientClientForm( Patient patient){
        patientid=patient.getId();
    }


    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }
}
