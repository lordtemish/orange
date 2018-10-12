package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Event;

import javax.validation.constraints.NotNull;

public class EventListForm implements  Comparable<EventListForm>{
    Event event;
    long chosenTime;
    public EventListForm(){}
    public EventListForm(Event e, long l)
    {
      event=e;
      chosenTime=l;
    }

    public void setChosenTime(long chosenTime) {
        this.chosenTime = chosenTime;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getChosenTime() {
        return chosenTime;
    }

    public Event getEvent() {
        return event;
    }
    @Override
    public int compareTo(@NotNull EventListForm comparestu) {
        long chosen=comparestu.getChosenTime();
        /* For Ascending order*/
        return Integer.parseInt((this.getChosenTime()-chosen)+"");

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
