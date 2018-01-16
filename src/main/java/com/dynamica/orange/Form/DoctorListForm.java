package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.ArrayList;

/**
 * Created by lordtemich on 1/16/18.
 */
public class DoctorListForm {
    public String name;
    public String surname;
    public String serviceInfo;
    public double rate;
    public DoctorListForm(Doctor doctor, Client client, ServiceType serviceType, ArrayList<Service> services){
        name=client.getName();
        surname=client.getSurname();
        double j=0;
        for(Rate i:doctor.getRates()){
            j+=i.getNum();
        }
        rate=j/doctor.getRates().size();
        switch (client.getLang()) {
            case "K":
                serviceInfo += serviceType.getNameKaz();
                for(Service i:services){
                    serviceInfo+=", "+i.getInfoKaz();
                }
                    break;
            default:
                serviceInfo += serviceType.getNameRus();
                for(Service i:services){
                    serviceInfo+=", "+i.getInfoRus();
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
}
