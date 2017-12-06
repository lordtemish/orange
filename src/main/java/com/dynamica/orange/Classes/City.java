package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class City {
    @Id
    private String id;
    private String nameKaz;
    private String nameRus;
    public City(){}
    public City(String rus, String kaz){nameRus=rus;nameKaz=kaz;}

    public String getId() {
        return id;
    }

    public String getNameKaz() {
        return nameKaz;
    }

    public String getNameRus() {
        return nameRus;
    }

    public void setNameRus(String nameRus) {
        this.nameRus = nameRus;
    }

    public void setNameKaz(String nameKaz) {
        this.nameKaz = nameKaz;
    }
}
