package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Doctor;

/**
 * Created by lordtemich on 12/12/17.
 */
public class ClientWithDoctorForm {
    Client client;
    Doctor doctor;

    public ClientWithDoctorForm(Client client, Doctor doctor){
        this.client=client;
        this.doctor=doctor;
    }

    public Client getClient() {
        return client;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
