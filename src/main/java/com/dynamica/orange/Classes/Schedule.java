package com.dynamica.orange.Classes;


import com.dynamica.orange.Form.ScheduleForm;

import java.util.List;

/**
 * Created by lordtemich on 1/3/18.
 */
public class Schedule {

    private List<ScheduleForm> monday;
    private List<ScheduleForm> tuesday;
    private List<ScheduleForm> wednesday;
    private List<ScheduleForm> thursday;
    private List<ScheduleForm> friday;
    private List<ScheduleForm> saturday;
    private List<ScheduleForm> sunday;
    public Schedule(){
    }
    public void setFriday(List<ScheduleForm> friday) {
        this.friday = friday;
    }

    public void setMonday(List<ScheduleForm> monday) {
        this.monday = monday;
    }

    public void setSaturday(List<ScheduleForm> saturday) {
        this.saturday = saturday;
    }

    public void setThursday(List<ScheduleForm> thursday) {
        this.thursday = thursday;
    }

    public void setSunday(List<ScheduleForm> sunday) {
        this.sunday = sunday;
    }

    public void setTuesday(List<ScheduleForm> tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(List<ScheduleForm> wednesday) {
        this.wednesday = wednesday;
    }

    public List<ScheduleForm> getFriday() {
        return friday;
    }

    public List<ScheduleForm> getMonday() {
        return monday;
    }

    public List<ScheduleForm> getSaturday() {
        return saturday;
    }

    public List<ScheduleForm> getSunday() {
        return sunday;
    }

    public List<ScheduleForm> getThursday() {
        return thursday;
    }

    public List<ScheduleForm> getTuesday() {
        return tuesday;
    }

    public List<ScheduleForm> getWednesday() {
        return wednesday;
    }
}
