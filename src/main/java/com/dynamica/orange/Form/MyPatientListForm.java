package com.dynamica.orange.Form;

public class MyPatientListForm {
    String patientid;
    boolean online;
    String name;
    String surname;
    Object photo;
    int calls;
    int commings;
    public MyPatientListForm(){}

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientid() {
        return patientid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Object getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public int getCalls() {
        return calls;
    }

    public int getCommings() {
        return commings;
    }

    public boolean isOnline() {
        return online;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public void setCalls(int calls) {
        this.calls = calls;
    }

    public void setCommings(int commings) {
        this.commings = commings;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
