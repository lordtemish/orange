package com.dynamica.orange.Classes;

public class TextObject {
    private String text;
    public TextObject(){

    }
    public  TextObject(String s){
        text=s;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
