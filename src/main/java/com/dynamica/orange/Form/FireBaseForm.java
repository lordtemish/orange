package com.dynamica.orange.Form;

import java.io.Serializable;

public class FireBaseForm {
    String to;
    Object data;
    Object notification;
    public  FireBaseForm(){

    }
    public FireBaseForm(String to, Object data, Object notification){
        this.to=to;this.data=data;this.notification=notification;
    }

    public String getTo() {
        return to;
    }

    public Object getData() {
        return data;
    }

    public Object getNotification() {
        return notification;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setNotification(Object notification) {
        this.notification = notification;
    }
}
/*
{
   "to" : "dohn93HCTVE:APA91bHooluW9b_jgMrSPbELIhEfQ2IUB4U0S20bJcpMUr46mwRKZYK8gg2hrgiAh4X-6nqLRkPBt9MJlbXqLkAzSPn7c2edGIwut0EyBg9LV7m1WdapPM5QrR_ZLabsm8oO4Zfhqfnu",
   "data" : {
     "resultInfo": {
      "code": "LNIS-0000",
      "message": "Процесс уже завершен"
     },
        "requestType" : "LOAN",
        "profileId" : "85781",
        "requestId" : "809",
        "title" : "Заявка",
        "subTitle" : "клиент: ЕСИЛБАЕВ ДУЛАТ, скоринг одобрен"

   },
  "notification" : {
       "title" : "Заявка",
       "body" : "клиент:ЕСИЛБАЕВ ДУЛАТ скоринг одобрен",
       "sound" : "default"
  }
}
 */