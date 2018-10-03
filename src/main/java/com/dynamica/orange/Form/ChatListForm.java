package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.*;

import java.util.Date;

/**
 * Created by lordtemich on 1/21/18.
 */
public class ChatListForm {
    String chatid;
    String status;
    int unread;
    MessageForm message;
    ChatRequest chatRequest;
    public String serviceInfo="";
    String name;
    String surname;
    String dadname;
    Object photo;
    boolean online;
    public ChatListForm(Chat chat, MessageForm message, Patient patient, Client client){
        this.chatid=chat.getId();
        this.status=chat.getStatus();
        this.unread=chat.getUnread();
        chatRequest=chat.getChatRequest();
        this.message=message;
        if(client.getDadname()==null){
            client.setDadname("");
        }
        if((new Date().getTime())-client.getLastOnline()<=600000) {
            online=true;
        }
        else{
            online=false;
        }
        name=client.getName();
        surname=client.getSurname();
        dadname=client.getDadname();
        if(client.getPhotourl().size()>0)
        photo=client.getPhotourl().get(client.getPhotourl().size()-1);
    }
    public ChatListForm(Chat chat, MessageForm message, Doctor doctor, Client client,String serviceInfo){
        this.chatid=chat.getId();
        this.status=chat.getStatus();
        this.unread=chat.getUnread();
        chatRequest=chat.getChatRequest();
        this.message=message;
        this.serviceInfo=serviceInfo;
        if((new Date().getTime())-client.getLastOnline()<=600000) {
            online=true;
        }
        else{
            online=false;
        }
        if(client.getDadname()==null){
            client.setDadname("");
        }
        name=client.getName();
        surname=client.getSurname();
        dadname=client.getDadname();
        if(client.getPhotourl().size()>0)
            photo=client.getPhotourl().get(client.getPhotourl().size()-1);
    }

    public ChatRequest getChatRequest() {
        return chatRequest;
    }

    public void setChatRequest(ChatRequest chatRequest) {
        this.chatRequest = chatRequest;
    }

    public String getServiceInfo() {
        return serviceInfo;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Object getPhoto() {
        return photo;
    }

    public int getUnread() {
        return unread;
    }

    public String getChatid() {
        return chatid;
    }

    public String getStatus() {
        return status;
    }

    public MessageForm getMessage() {
        return message;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getDadname() {
        return dadname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setPhoto(Object photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }
}
