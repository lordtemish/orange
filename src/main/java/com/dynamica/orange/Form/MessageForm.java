package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Doctor;
import com.dynamica.orange.Classes.Message;
import com.dynamica.orange.Classes.Patient;

public class MessageForm {
    Message message;
    boolean mymessage;
    public MessageForm(Message message, Client client, Patient patient){
        message.setClientinfo(new PatientClientForm(patient));
        this.message=message;
    }
    public MessageForm(Message message, Client client, Doctor doctor){
        message.setClientinfo(new DoctorClientForm(doctor));
        this.message=message;
    }

    public boolean isMymessage() {
        return mymessage;
    }

    public void setMymessage(boolean mymessage) {
        this.mymessage = mymessage;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
