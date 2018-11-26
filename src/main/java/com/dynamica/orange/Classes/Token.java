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
    private String iosToken, androidToken;
    private long curtimestart;
    private String ip;
    private boolean admin;
    public Token(){
        curtimestart=new Date().getTime();
    }
    public Token(String clientid){
        curtimestart=new Date().getTime();
        this.clientid=clientid;
        admin=false;
    }
    public Token(String clientid, String ip){
        curtimestart=new Date().getTime();
        this.clientid=clientid;
        this.ip=ip;
        admin=false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAndroidToken(String androidToken) {
        this.androidToken = androidToken;
    }

    public void setIosToken(String iosToken) {
        this.iosToken = iosToken;
    }

    public String getAndroidToken() {
        return androidToken;
    }

    public String getIosToken() {
        return iosToken;
    }

    public String getIp() {
        return ip;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
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
