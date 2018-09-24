package com.dynamica.orange.Form;

import java.util.List;

public class PatientRequestForm {
    DoctorListForm doctor;
    String type;
    String accepted;
    public  PatientRequestForm(DoctorListForm form,String type,String accepted){
        doctor=form;
        this.type=type;
        this.accepted=accepted;
    }

    public String getType() {
        return type;
    }

    public DoctorListForm getDoctor() {
        return doctor;
    }

    public String getAccepted() {
        return accepted;
    }
}
