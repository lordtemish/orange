package com.dynamica.orange.Classes;


import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by lordtemich on 12/28/17.
 */
public class Token {
    @Id
    private String id;
    private String clientid;
    private long curtimestart;
    private String ip;
    public Token(){
        curtimestart=new Date().getTime();
    }
    public Token(String clientid){
        curtimestart=new Date().getTime();
        this.clientid=clientid;
    }
    public Token(String clientid, String ip){
        curtimestart=new Date().getTime();
        this.clientid=clientid;
        this.ip=ip;
    }

    public long getCurtimestart() {
        return curtimestart;
    }

    public String getClientid() {
        return clientid;
    }
    public String getId() {
        return id;
    }
    public void setClientid(String id){
        clientid=id;
    }
    public void setIp(String ip){
        this.ip=ip;
    }
}
