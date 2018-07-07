package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Doctor;

public class DoctorClientForm {
    private String doctorid;

    public DoctorClientForm(Doctor doctor){

            doctorid=doctor.getId();
    }

    public String getDoctorid() {
        return doctorid;
    }
    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }
}
