package com.dynamica.orange.Form;

public class ChatIdForm {
    public String chatid;
    public Object myphoto;
    public ChatIdForm(String c, Object myphoto){
        chatid=c;
        this.myphoto=myphoto;
    }

    public Object getMyphoto() {
        return myphoto;
    }

    public String getChatid() {
        return chatid;
    }
}
