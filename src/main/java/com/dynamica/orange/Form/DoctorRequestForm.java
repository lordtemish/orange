package com.dynamica.orange.Form;

import java.util.List;

public class DoctorRequestForm {
    List<MyPatientListForm> requests;
    List<MyPatientListForm> phoneRequests;
    public DoctorRequestForm(List<MyPatientListForm> requests,List<MyPatientListForm> phoneRequests){
        this.requests=requests;
        this.phoneRequests=phoneRequests;
    }

    public List<MyPatientListForm> getRequests() {
        return requests;
    }

    public List<MyPatientListForm> getPhoneRequests() {
        return phoneRequests;
    }
}
