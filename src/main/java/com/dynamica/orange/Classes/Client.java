package com.dynamica.orange.Classes;

import com.dynamica.orange.Form.FileObjectForm;
import org.springframework.data.annotation.Id;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lordtemich on 10/27/17.
 */
public class Client {
    @Id
    private String id;
    private String phone;
    private String name;
    private String surname;
    private boolean activated=false;
    private String accesscode;
    private String dadname;
    private String gender;
    private String email;
    private String password;
    private String lang;
    private boolean push, publ;
    ArrayList<String> phones;
    ArrayList<String> mails;
    ArrayList<Object> photourl;
    private long lastOnline;

    public long getLastOnline() {
        return lastOnline;
    }
    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }
    public void onReqested(){
        this.lastOnline=new Date().getTime();
    }
    public Client(){
        phones=new ArrayList<>();mails=new ArrayList<>();
        photourl=new ArrayList<>();
        lang="R";
    }
    public Client(String mail){
        this.email=mail;
        phones=new ArrayList<>();mails=new ArrayList<>();photourl=new ArrayList<>();
        lang="R";
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public boolean isActivated() {
        return activated;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode = accesscode;
    }

    public Client(String phone, String name, String surname, String dadname, String email, String password, String lang){
        this.phone=phone;
        this.name=name;
        this.surname=surname;
        this.dadname=dadname;this.email=email;this.password=password;this.lang=lang;
        phones=new ArrayList<>();mails=new ArrayList<>();photourl=new ArrayList<>();
    }

    public void setGender(String gender) {this.gender = gender;}
    public void setPhone(String phone){this.phone=phone;}
    public void setName(String name){this.name=name;}
    public void setSurname(String surname){this.surname=surname;}
    public void setDadname(String dadname){this.dadname=dadname;}
    public void setEmail(String email){this.email=email;}
    public void setPassword(String password){
        this.password=password;
    }
    public void setLang(String lang){this.lang=lang;}
    public void addPhone(String phone){
        phones.add(phone);
    }
    public void addEmail(String email){
        mails.add(email);
    }
    public void setPush(boolean b){
        push=b;
    }
    public void setPubl(boolean b){
        publ=b;
    }
    public void addPhoto(Object url){
        photourl.clear();
        photourl.add(url);
    }
    public void deletePhoto(){
        photourl.clear();
    }

    public void setMails(ArrayList<String> mails) {
        this.mails = mails;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }

    public boolean deletePhoto(String id){

        for(Object i:photourl){
            try {
                FileObjectForm ii = (FileObjectForm) i;
                if (ii.getId().equals(id)) {
                    photourl.remove(i);
                    return true;
                }
            }
            catch (Exception e){
                continue;
            }
        }
        return false;
    }
    public boolean deleteMail(String mail){
        if(mails.contains(mail)){
            mails.remove(mail);
            return true;
        }
        else return false;
    }
    public boolean deletePhone(String phone){
        if(phones.contains(phone)){
            phones.remove(phone);
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isPubl() {
        return publ;
    }

    public boolean isPush() {
        return push;
    }
    public String getId(){return id;}
    public String getPhone(){return phone;}
    public String getName(){return name;}
    public String getSurname(){return surname;}
    public String getDadname(){return dadname;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}
    public String getGender() {return gender;}
    public ArrayList<String> getPhones(){return phones;}
    public boolean setPhoneByIndex(int i, String s){
        if(phones.size()>i) {
            phones.set(i, s);
                    return true;
        }
        else
            return false;
    }
    public boolean setMailByIndex(int i, String s){
        if(mails.size()>i) {
            mails.set(i, s);
            return true;
        }
        else
            return false;
    }
    public ArrayList<String> getMails(){return mails;}

    public ArrayList<Object> getPhotourl() {
        return photourl;
    }

    public String getLang() {
        return lang;
    }

    public void setPhotourl(ArrayList<Object> photourl) {
        this.photourl = photourl;
    }
}
