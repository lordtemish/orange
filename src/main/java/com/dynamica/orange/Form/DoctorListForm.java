package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;

/**
 * Created by lordtemich on 1/16/18.
 */
public class DoctorListForm {
    public String doctorid;
    public String name;
    public String surname;
    public String serviceInfo="";
    public double rate;
    public DoctorListForm(Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services){
        if(doctor!=null) {
            doctorid = doctor.getId();
        }
        else{
            doctorid=null;
            doctor=new Doctor(null,null,null);
        }
        if(client!=null) {
            name = client.getName();
            surname = client.getSurname();
            if(client.getLang()==null){
                client.setLang("R");
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
}
