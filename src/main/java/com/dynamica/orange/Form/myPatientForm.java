package com.dynamica.orange.Form;

public class myPatientForm {
    boolean phonedoctor;
    boolean phone;
    boolean accepted;
    String id;
    public myPatientForm(boolean phone,boolean accepted, String id){
        this.phone=phone;
        phonedoctor=false;
        this.accepted=accepted;
        this.id=id;
    }

    public void setPhonedoctor(boolean phonedoctor) {
        this.phonedoctor = phonedoctor;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setId(String id) {
        this.id = id;
    }
g
    public boolean isPhonedoctor() {
        return phonedoctor;
    }

    public String getId() {
        return id;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }
}
