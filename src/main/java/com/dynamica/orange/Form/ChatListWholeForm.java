package com.dynamica.orange.Form;

import java.util.List;

public class ChatListWholeForm {
    Object myphoto;
    List<ChatListForm> results;
    public ChatListWholeForm(){}

    public void setResults(List<ChatListForm> results) {
        this.results = results;
    }

    public void setMyphoto(Object myphoto) {
        this.myphoto = myphoto;
    }

    public List<ChatListForm> getResults() {
        return results;
    }

    public Object getMyphoto() {
        return myphoto;
    }
}