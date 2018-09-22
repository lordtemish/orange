package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Comment;
import com.dynamica.orange.Classes.Doctor;
import com.dynamica.orange.Classes.Patient;

/**
 * Created by lordtemich on 1/16/18.
 */
public class CommentForm {
    String name;
    String surname;
    long time;
    String text;
    boolean impression;
    int rate;
    public CommentForm(Patient patient, Client client, Comment comment){
        name=client.getName();
        surname=client.getSurname();
        time=comment.getCreatedTime();
        text=comment.getText();
        impression=comment.isImpression();
        rate=comment.getRate();
    }

    public int getRate() {
        return rate;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public String getText() {
        return text;
    }

    public boolean isImpression() {
        return impression;
    }
}