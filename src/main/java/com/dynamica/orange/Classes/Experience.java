package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Experience {
    @Id
    private String id;
    private String namecom;
    private String position;
    private int years;
    private String start_year;
    public Experience(){

    }
    public Experience(String namecom, String position, int years, String start_year){
        this.namecom=namecom;
        this.position=position;
        this.years=years;
        this.start_year=start_year;
    }
    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public void setNamecom(String namecom) {
        this.namecom = namecom;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setYears(int years) {
        this.years = years;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public int getYears() {
        return years;
    }

    public String getNamecom() {
        return namecom;
    }

    public String getPosition() {
        return position;
    }

    public String getStart_year() {
        return start_year;
    }
}
