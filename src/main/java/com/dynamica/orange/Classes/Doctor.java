package com.dynamica.orange.Classes;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Doctor {
    //Logger log=Logger.getLogger(Doctor.class);
    @Id
    private String id;
    private String clientid;
        private String position;
        private String info;
    private Address homeAddress;
    private Address workAddress;
    private Object servicetypeid;
    private ArrayList<Education> educations;
    private String serv_type;
    private ArrayList<String> certificates;
    private ArrayList<String> services;
    private ArrayList<Rate> rates;
    private ArrayList<OwnService> owns;
    private ArrayList<Experience> experiences;
    private ArrayList<String> profachievments;
    private ArrayList<String> extrainfo;
    private ArrayList<Comment> comments;
    private Schedule workSchedule;
    private Schedule homeSchedule;
    public Doctor(){
        educations=new ArrayList<>();
        services=new ArrayList<>();
        rates=new ArrayList<>();
        owns=new ArrayList<>();
        experiences=new ArrayList<>();
        profachievments=new ArrayList<>();
        extrainfo=new ArrayList<>();
        comments=new ArrayList<>();
        certificates=new ArrayList<>();
    }
    public Doctor(String client_id, String position, String info){
        this.clientid=client_id;
        this.position=position;
        this.info=info;
        educations=new ArrayList<>();
        services=new ArrayList<>();
        rates=new ArrayList<>();
        owns=new ArrayList<>();
        experiences=new ArrayList<>();
        profachievments=new ArrayList<>();
        extrainfo=new ArrayList<>();
        comments=new ArrayList<>();
        certificates=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setClientid(String client_id) {
        this.clientid = client_id;
    }



    public Object getServicetypeid() {
        return servicetypeid;
    }

    public void setServicetypeid(Object service_type_id) {
        this.servicetypeid = service_type_id;
    }


    public String getClientid() {
        return clientid;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
    public void addComment(Comment s){
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        comments.add(s);
    }
    public boolean deleteComment(Comment s){
        if(comments.contains(s)){
            comments.remove(s);
            return true;
        }
        else
            return false;
    }

    public ArrayList<IDObject> getCertificates() {
        ArrayList<IDObject> idObjects=new ArrayList<>();
        for(String i:certificates){
            idObjects.add(new IDObject(i));
        }
        return idObjects;
    }
    public void addCertificate(String url){
        certificates.add(url);
    }
    public boolean deleteCertificate(String url){
        if(certificates.contains(url)){
            certificates.remove(url);
            return true;
        }
        else return false;
    }
    public ArrayList<Education> getEducations() {
        return educations;
    }
    public void addEducation(Education s){
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        educations.add(s);
    }
    public Education getEducationById(String id){
        for(Education i:educations){
           // log.info(i.getId()+" "+id);
                if (i.getId().equals(id)) {
                    return i;
                }
        }
        return null;
    }
    public void setEducationById(String id, Education education){
        int j=0;
        for(Education i:educations){
            if(i.getId().equals(id)){
                educations.set(j, education) ;
                break;
            }
            j++;
        }
    }
    public boolean deleteEducationUrlById(String id, String url){
        int j=0;
        for(Education i:educations){
            if(i.getId().equals(id)){
                if(i.getUrls().contains(url)){
                    i.getUrls().remove(url);
                    return true;
                }
                else{
                    return false;
                }
            }
            j++;
        }
        return false;
    }
    public boolean deleteEducation(Education s){
        if(educations.contains(s)){
            educations.remove(s);
            return true;
        }
        else
            return false;
    }
    public boolean deleteExperience(Experience s){
        if(experiences.contains(s)){
            experiences.remove(s);
            return true;
        }
        else
            return false;
    }
    public ArrayList<Experience> getExperiences() {
        return experiences;
    }
    public void deleteExperiencebyId(String id){
        for(Experience i:experiences){
            if(i.getId().equals(id)){
                experiences.remove(i);
                break;
            }
        }
    }
    public void updateExperiencebyId(String id, String namecom, String position, int years, String startyear){
        for(Experience i:experiences){
            if(i.getId().equals(id)){
                i.setNamecom(namecom);
                i.setPosition(position);
                i.setStart_year(startyear);
                i.setYears(years);
                break;
            }
        }
    }
    public void addExperience(Experience s){
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        experiences.add(s);
    }

    public ArrayList<OwnService> getOwns() {
        return owns;
    }
    public String getHomePlaceOwn(){
        for(OwnService i:owns){
            if(i.getHomeplace()){
                return i.getId()+" ";
            }
        }
        return "";
    }
    public void addOwnService(OwnService s){
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        owns.add(s);
    }
    public boolean deleteOwnService(OwnService s){
        if(owns.contains(s)){
            owns.remove(s);
            return true;
        }
        else
            return false;
    }

    public ArrayList<Rate> getRates() {
        return rates;
    }
    public void setRate(Rate s){
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        boolean fl=true;
        for(Rate rate:rates){
            if(s.getPatient_id().equals(rate.getPatient_id())){
                fl=false;
                rate.setNum(s.getNum());
            }
        }
        if(fl)
        rates.add(s);
    }
    public boolean deleteRate(Rate s){
        if(rates.contains(s)){
            rates.remove(s);
            return true;
        }
        else
            return false;
    }
    public ArrayList<IDObject> getServices() {
        ArrayList<IDObject> idObjects=new ArrayList<>();
        for(String i:services){
            idObjects.add(new IDObject(i));
        }
        return idObjects;
    }
    public ArrayList<String> getServicesList() {
        return services;
    }
    public boolean deleteService(Service s){
        if(services.contains(s)){
            services.remove(s);
            return true;
        }
        else
            return false;
    }
    public void addService(String s){
        if(!services.contains(s))services.add(s);
    }
    public void clearServices(){
        services.clear();
    }

    public ArrayList<TextObject> getExtrainfo() {
        ArrayList<TextObject> idObjects=new ArrayList<>();
        for(String i:extrainfo){
            idObjects.add(new TextObject(i));
        }
        return idObjects;
    }
    public boolean deleteExtraInfo(String s){
        if(extrainfo.contains(s)){
            extrainfo.remove(s);
            return true;
        }
        else
            return false;
    }
    public void addExtraInfo(String s){
        extrainfo.add(s);
    }

    public ArrayList<TextObject> getProfachievments() {
        ArrayList<TextObject> idObjects=new ArrayList<>();
        for(String i:profachievments){
            idObjects.add(new TextObject(i));
        }
        return idObjects;
    }
    public void addProfAch(String s){
        profachievments.add(s);
    }
    public boolean deleteProfAchs(String s){
        if(profachievments.contains(s)){
            profachievments.remove(s);
            return true;
        }
        else
            return false;
    }


    public String getPosition() {
        return position;
    }

    public String getServ_type() {
        return serv_type;
    }


    public Address getHomeAddress() {
        return homeAddress;
    }

    public Address getWorkAddress() {
        return workAddress;
    }

    public void setHomeAddress(Address s) {
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        this.homeAddress = s;
    }

    public void setWorkAddress(Address s) {
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        this.workAddress = s;
    }

    public void setServ_type(String serv_type) {
        this.serv_type = serv_type;
    }



    public Schedule getHomeSchedule() {
        return homeSchedule;
    }

    public Schedule getWorkSchedule() {
        return workSchedule;
    }

    public void setHomeSchedule(Schedule homeSchedule) {
        this.homeSchedule = homeSchedule;
    }

    public void setWorkSchedule(Schedule workSchedule) {
        this.workSchedule = workSchedule;
    }
}
