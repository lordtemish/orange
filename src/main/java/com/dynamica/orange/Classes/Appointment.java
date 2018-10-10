package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

public class Appointment {
    @Id
    private String id;
    private long day;
    private int start, stop;
    private String doctorid;
    private String orderid;
    private String eventid;
    private boolean order, accepted;

    public Appointment(){}
    public Appointment(long day,int start, int stop, String doctorid){
        this.day=day;
        this.start=start;
        this.stop=stop;
        this.doctorid=doctorid;
        this.accepted=false;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getEventid() {
        return eventid;
    }

    public String getOrderid() {
        return orderid;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isOrder() {
        return order;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public int getStart() {
        return start;
    }

    public int getStop() {
        return stop;
    }

    public long getDay() {
        return day;
    }
}
