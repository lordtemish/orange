package com.dynamica.orange.Form;

import org.springframework.data.annotation.Id;

public class ScheduleForm {
    @Id
    private String id;
    private int from;
    private int to;
    private boolean empty;
    public ScheduleForm(){}
    public ScheduleForm(int f, int t){
        from=f;
        to=t;
        empty=true;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
