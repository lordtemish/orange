package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Patient;
import com.dynamica.orange.Repo.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lordtemich on 12/8/17.
 */
public class ClientWithPatientForm {
    Patient patient;
    Client client;
    public ClientWithPatientForm(Client client, Patient patient){
        this.client=client;
        this.patient=patient;
    }

    public Client getClient() {
        return client;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
