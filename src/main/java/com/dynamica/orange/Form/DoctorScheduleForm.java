package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Schedule;

import java.util.List;

public class DoctorScheduleForm {
    List<ScheduleForm> work, home;
    public DoctorScheduleForm(){}
    public DoctorScheduleForm(List<ScheduleForm> w, List<ScheduleForm> h){
        work=w;
        home=h;
    }

    public List<ScheduleForm> getHome() {
        return home;
    }

    public List<ScheduleForm> getWork() {
        return work;
    }

    public void setHome(List<ScheduleForm> home) {
        this.home = home;
    }

    public void setWork(List<ScheduleForm> work) {
        this.work = work;
    }
}
