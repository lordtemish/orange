package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by lordtemich on 1/3/18.
 */
public class Message {
    @Id
    private String id;
    private Object clientid;
    private String type;
    private Object info;
    private long time;
    private boolean read=false;
    private Object clientinfo;
    public Message(){
        time=new Date().getTime();
    }
    public Message(String clientid){this.clientid =clientid;time=new Date().getTime();}
    public Message(String clientid, String type){this.clientid =clientid;this.type=type;time=new Date().getTime();}
    public Message(String clientid, String type, String info){this.clientid =clientid;this.type=type;this.info=info;time=new Date().getTime();}
    public Message(String clientid, String type, String info, long time){this.clientid =clientid;this.type=type;this.info=info;this.time=time;time=new Date().getTime();}


    public void setClientinfo(Object clientinfo) {
        this.clientinfo = clientinfo;
    }

    public Object getClientinfo() {
        return clientinfo;
    }

    public void setClientid(Object clientid) {
        this.clientid = clientid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public Object getClientid() {
        return clientid;
    }

    public Object getInfo() {
        return info;
    }

    public String getType() {
        return type;
    }
}
