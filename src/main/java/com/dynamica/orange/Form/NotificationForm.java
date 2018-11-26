package com.dynamica.orange.Form;

public class NotificationForm {
    String title;
    String body;
    String sound;
    public NotificationForm(String t, String b, String s){
        title=t;
        body=b;
        sound=s;
    }

    public String getBody() {
        return body;
    }

    public String getSound() {
        return sound;
    }

    public String getTitle() {
        return title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
