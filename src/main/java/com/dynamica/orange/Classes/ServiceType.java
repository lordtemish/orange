package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class ServiceType {
    @Id
    private String id;
    private String nameRus;
    private String nameKaz;
    public ServiceType(){}
    public ServiceType(String rus, String kaz){
        nameRus=rus;
        nameKaz=kaz;
    }
    public String getId() {
        return id;
    }

    public String getNameRus() {
        if(nameRus!=null) {
            return nameRus;
        }
        else{
            return "";
        }
    }

    public void setNameKaz(String nameKaz) {
        this.nameKaz = nameKaz;
    }

    public void setNameRus(String nameRus) {
        this.nameRus = nameRus;
    }

    public String getNameKaz() {
        if(nameKaz!=null) {
            return nameKaz;
        }
        else{
            return "";
        }
    }
}
