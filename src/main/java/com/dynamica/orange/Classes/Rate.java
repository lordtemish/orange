package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Rate {
    @Id
    private String id;
    private String client_id;
    private int num;
    public Rate(String client_id, int num){
        this.client_id=client_id;
        this.num=num;
    }

    public String getId() {
        return id;
    }

    public int getNum() {
        return num;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
