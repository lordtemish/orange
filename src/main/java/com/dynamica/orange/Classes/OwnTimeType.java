package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/28/17.
 */
public class OwnTimeType {
    @Id
    private String id;
    private String namekaz;
    private String namerus;
    public OwnTimeType(){}
    public OwnTimeType(String rus,String kaz){
        namerus=rus;
        namekaz=kaz;
    }
    public String getId() {
        return id;
    }

    public String getNamekaz() {
        return namekaz;
    }

    public String getNamerus() {
        return namerus;
    }

    public void setNamekaz(String namekaz) {
        this.namekaz = namekaz;
    }

    public void setNamerus(String namerus) {
        this.namerus = namerus;
    }
}
