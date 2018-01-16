package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Comment {
    @Id
    private String id;
    private String patient_id;
    private String text;
    private String date;
    private String time;
  /*  private SimpleDateFormat sdf=new SimpleDateFormat("dd.mm.yyyy");
    private SimpleDateFormat sdh=new SimpleDateFormat("HH:mm");*/
    private long createdTime;
    private boolean impression;
    public Comment(){
        Calendar calendar=Calendar.getInstance();
        Date date=calendar.getTime();
       /* this.date=sdf.format(date);
        this.time=sdh.format(date);*/
        createdTime=date.getTime();
    }
    public Comment(String patient_id,String text){
        Calendar calendar=Calendar.getInstance();
        this.patient_id=patient_id;
        this.text=text;
        Date date=calendar.getTime();
        /*this.date=sdf.format(date);
        this.time=sdh.format(date);*/
        createdTime=date.getTime();
    }
    public String getId() {
        return id;
    }

    public void setImpression(boolean impression) {
        this.impression = impression;
    }

    public boolean isImpression() {
        return impression;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public String getDate() {
        return date;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
