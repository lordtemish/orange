package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Event {
    @Id
    private String id;
    private String doctorid;
    private int num;
    private long createdTime, chosenTime;
    private boolean atwork;
    List<Object> patientids;
    private double notifyevery;
    private String info, FIO;
    public Event(String doctorid, int num, long chosenTime, boolean atwork, double notifyevery, String info, String FIO){
        this.doctorid=doctorid;
        this.num=num;
        this.chosenTime=chosenTime;
        this.createdTime=new Date().getTime();
        this.atwork=atwork;
        this.notifyevery=notifyevery;
        this.info=info;
        this.FIO=FIO;
    }

    public List<Object> getPatientids() {
        return patientids;
    }
    public void setPatientidsString(List<String> ownServices) {
        List<Object> string=new ArrayList<>();
        for(int i=0;i<ownServices.size();i++) {
            while(ownServices.get(i).contains("[") || ownServices.get(i).contains("]") || ownServices.get(i).contains("\"") || ownServices.get(i).contains("\'")){
                for(int j=0;j<ownServices.get(i).length();j++){
                    ArrayList<String> a=new ArrayList<>();
                    a.add("[");a.add("]");a.add("\'");a.add("\"");
                    if(a.contains(ownServices.get(i).substring(j,j+1))){
                        if(j>0 && j<ownServices.get(i).length()-1) {
                            ownServices.set(i,ownServices.get(i).substring(0,j)+ownServices.get(i).substring(j+1,ownServices.get(i).length()));
                        }
                        else if(j==0){
                            ownServices.set(i,ownServices.get(i).substring(j+1,ownServices.get(i).length()));
                        }
                        else{
                            ownServices.set(i,ownServices.get(i).substring(0,j));
                        }
                    }
                }
            }
            if(ownServices.get(i).length()<1)
                continue;
            string.add(ownServices.get(i));
        }
        this.patientids = string;
    }
    public void setPatientids(List<Object> patientids) {
        this.patientids = patientids;
    }

    public double getNotifyevery() {
        return notifyevery;
    }

    public int getNum() {
        return num;
    }

    public long getChosenTime() {
        return chosenTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public String getFIO() {
        return FIO;
    }

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public void setAtwork(boolean atwork) {
        this.atwork = atwork;
    }

    public void setChosenTime(long chosenTime) {
        this.chosenTime = chosenTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setNotifyevery(double notifyevery) {
        this.notifyevery = notifyevery;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isAtwork() {
        return atwork;
    }
}
