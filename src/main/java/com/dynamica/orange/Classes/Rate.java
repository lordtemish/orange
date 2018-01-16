package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Rate {
    @Id
    private String id;
    private String patient_id;
    private int num;
    public Rate(String patient_id, int num){
        this.patient_id=patient_id;
        this.num=num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
