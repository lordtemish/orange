package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Chat;
import com.dynamica.orange.Classes.Client;
import com.dynamica.orange.Classes.Message;

/**
 * Created by lordtemich on 1/21/18.
 */
public class ChatListForm {
    String chatid;
    String status;
    int unread;
    MessageForm message;
    String namesurname;
    Object photo;
    public ChatListForm(Chat chat, MessageForm message, Client client){
        this.chatid=chat.getId();
        this.status=chat.getStatus();
        this.unread=chat.getUnread();
        this.message=message;
        if(client.getDadname().equals(null)){
            client.setDadname("");
        }
        namesurname=client.getSurname()+" "+client.getName()+" "+client.getDadname();
        if(client.getPhotourl().size()>0)
        photo=client.getPhotourl().get(client.getPhotourl().size()-1);
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

    public String getNamesurname() {
        return namesurname;
    }
}
