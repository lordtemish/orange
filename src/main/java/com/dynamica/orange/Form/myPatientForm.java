package com.dynamica.orange.Form;

public class myPatientForm {
    public boolean phonefinished=false;
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

    public void setPhonefinished(boolean phonecancelled) {
        this.phonefinished = phonecancelled;
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
