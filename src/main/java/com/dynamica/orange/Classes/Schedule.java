package com.dynamica.orange.Classes;


/**
 * Created by lordtemich on 1/3/18.
 */
public class Schedule {
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    public Schedule(){
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getFriday() {
        return friday;
    }

    public String getMonday() {
        return monday;
    }

    public String getSaturday() {
        return saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public String getThursday() {
        return thursday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }
}
