package com.dynamica.orange.Classes;

import org.springframework.data.annotation.Id;

import java.util.Stack;

/**
 * Created by lordtemich on 1/3/18.
 */
public class Chat {
    @Id
    private String id;
    private String doctorid;
    private String patientid;
    private String status;
    private int unread;
    Stack<Message> messages;
    public Chat(String doctorid, String patientid){
        this.doctorid=doctorid;
        this.patientid=patientid;
        messages=new Stack<>();
    }

    public int getUnread() {
        return unread;
    }

    public String getDoctorid() {
        return doctorid;
    }

    public String getId() {
        return id;
    }

    public String getPatientid() {
        return patientid;
    }

    public String getStatus() {
        return status;
    }

    public void setDoctorid(String doctorid) {
        this.doctorid = doctorid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }
    public void unreadPlus(){
        this.unread++;
    }
    public void addMessage(Message s){
        String id=s.getId();
        try {
            if (id.isEmpty()) {
                id = System.identityHashCode(s) + "";
            }
        }
        catch (NullPointerException e){
            id = System.identityHashCode(s) + "";
        }
        s.setId(id);
        messages.add(s);
    }

    public Stack<Message> getMessages() {
        return messages;
    }
    public Message getLastMessage(){return messages.get(messages.size()-1);}
}
