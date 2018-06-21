package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Doctor;
import com.dynamica.orange.Classes.Message;
import com.dynamica.orange.Classes.Patient;

public class MessageForm {
    Message message;
    public MessageForm(Message message, Client client, Patient patient){
        message.setClientid(new PatientClientForm(client,patient));
        this.message=message;
    }
    public MessageForm(Message message, Client client, Doctor doctor){
        message.setClientid(new DoctorClientForm(client,doctor));
        this.message=message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
