package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lordtemich on 1/16/18.
 */
public class  DoctorListForm {
    public String doctorid;
    public String name;
    public String surname;
    public String serviceInfo="";
    public double rate;
    public Object photo;
    boolean online;
    boolean publ=false;

    public void setPubl(boolean publ) {
        this.publ = publ;
    }

    public boolean isPubl() {
        return publ;
    }

    public DoctorListForm(Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services){
        if(doctor!=null) {
            doctorid = doctor.getId();
        }
        else{
            doctorid=null;
            doctor=new Doctor(null,null,null);
        }
        if(client!=null) {
            if(client.getLastOnline()-(new Date().getTime())<=600000){
                online=true;
            }
            else online=false;
            publ=client.isPubl();
            name = client.getName();
            surname = client.getSurname();
            if(client.getLang()==null){
                client.setLang("R");
            }
            if(client.getPhotourl().size()>0){
                photo=client.getPhotourl().get(client.getPhotourl().size()-1);
            }
        }
        else{
            name=null;surname=null;
            client=new Client(null);
            client.setLang("R");
        }
        if(serviceType==null){
            serviceType=new ServiceType("null","null");
        }
        if(services.size()>0){

        }
        double j=0;
        for(Rate i:doctor.getRates()){
            j+=i.getNum();
        }
        if(doctor.getRates().size()>0) {
            rate = j / doctor.getRates().size();
        }
        else{
            rate=-1;
        }
        switch (client.getLang()) {
            case "K":
                serviceInfo += serviceType.getNameKaz();
                for(Service i:services){
                    if(i!=null && i.getInfoKaz()!=null)
                    serviceInfo+=", "+i.getInfoKaz();
                    else{
                        serviceInfo+=", "+null;
                    }
                }
                    break;
            default:
                serviceInfo += serviceType.getNameRus();
                for(Service i:services){
                    if(i!=null && i.getInfoRus()!=null)
                    serviceInfo+=", "+i.getInfoRus();
                    else{
                        serviceInfo+=", "+null;
                    }
                }
        }
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public double getRate() {
        return rate;
    }

    public String getName() {
        return name;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public String getSurname() {
        return surname;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public Object getPhoto() {
        return photo;
    }
}
