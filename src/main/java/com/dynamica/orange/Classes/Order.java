package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lordtemich on 1/5/18.
 */
public class Order {
    @Id
    private String id;
    private String status;
    private String serviceTypeId;
    private String doctorid;
    private String patientid;
    private long createdTime;
    private long choseTime;
    private String periodTime;
    List<String> services;
    private boolean atwork;
    private double periodinhours;
    private String textMessage;
    private String photoMessage;
    private String audioMessage;
    private Address address;
    private String diagnosAnswer;
    private String healingAnswer;
    private String audiohealing;
    private String textAnswer;
    private String photoAnswer;
    private String audioAnswer;
    public Order(){
        createdTime=new Date().getTime();services=new ArrayList<>();
    }
    public Order(String doctorid, String patientid){
        this.doctorid=doctorid;
        this.patientid=patientid;
        createdTime=new Date().getTime();
        services=new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAudiohealing() {
        return audiohealing;
    }

    public void setAudiohealing(String audiohealing) {
        this.audiohealing = audiohealing;
    }

    public String getHealingAnswer() {
        return healingAnswer;
    }

    public void setHealingAnswer(String healingAnswer) {
        this.healingAnswer = healingAnswer;
    }

    public boolean isAtwork() {
        return atwork;
    }

    public double getPeriodinhours() {
        return periodinhours;
    }

    public List<String> getServices() {
        return services;
    }

    public long getChoseTime() {
        return choseTime;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public Address getAddress() {
        return address;
    }

    public String getPeriodTime() {
        return periodTime;
    }

    public String getAudioAnswer() {
        return audioAnswer;
    }
    public String getAudioMessage() {
        return audioMessage;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public String getPatientid() {
        return patientid;
    }

    public String getPhotoAnswer() {
        return photoAnswer;
    }

    public String getPhotoMessage() {
        return photoMessage;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public String getStatus() {
        return status;
    }

    public String getTextAnswer() {
        return textAnswer;
    }

    public String getTextMessage() {
        return textMessage;
    }



    public void setAtwork(boolean atwork) {
        this.atwork = atwork;
    }

    public void setPeriodTime(String periodTime) {
        this.periodTime = periodTime;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
    public void setAudioAnswer(String audioAnswer) {
        this.audioAnswer = audioAnswer;
    }

    public void setAudioMessage(String audioMessage) {
        this.audioMessage = audioMessage;
    }

    public void setChoseTime(long choseTime) {
        this.choseTime = choseTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public void setPeriodinhours(double periodinhours) {
        this.periodinhours = periodinhours;
    }

    public void setPhotoAnswer(String photoAnswer) {
        this.photoAnswer = photoAnswer;
    }

    public void setPhotoMessage(String photoMessage) {
        this.photoMessage = photoMessage;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }
    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTextAnswer(String textAnswer) {
        this.textAnswer = textAnswer;
    }

    public void setTextMessage(String textMessage) {
            this.textMessage = textMessage;
    }

    public String getDiagnosAnswer() {
        return diagnosAnswer;
    }

    public void setDiagnosAnswer(String diagnosAnswer) {
        this.diagnosAnswer = diagnosAnswer;
    }

    public void addService(String service){
        services.add(service);
    }
    public void addServices(String services){
        String[] alls=services.split(" ");
        for(int i=0;i<alls.length;i++){
            this.services.add(alls[i]);
        }
    }
}
