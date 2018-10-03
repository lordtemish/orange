package com.dynamica.orange.Form;

import com.dynamica.orange.Classes.Event;

import java.util.List;

public class EventWithOrdersForm {
    List<Object> events;
    List<Object> orders;
    List<Integer> days;

    public EventWithOrdersForm(List<Object> events ,List<Object> orders, List<Integer> days){
        this.events=events;
        this.orders=orders;
        this.days=days;
    }

    public List<Integer> getDays() {
        return days;
    }

    public List<Object> getEvents() {
        return events;
    }

    public List<Object> getOrders() {
        return orders;
    }
}
