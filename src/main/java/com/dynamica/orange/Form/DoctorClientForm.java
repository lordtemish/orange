package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Doctor;

public class DoctorClientForm {
    private String clientid;
    private String doctorid;
    private String name;
    private String surname;
    private String dadname;
    public DoctorClientForm(Client client, Doctor doctor){
            name=client.getName();
            surname=client.getSurname();
            dadname=client.getDadname();
            clientid=client.getId();
            doctorid=doctor.getId();
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getClientid() {
        return clientid;
    }

    public String getDadname() {
        return dadname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public void setDadname(String dadname) {
        this.dadname = dadname;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }
}
