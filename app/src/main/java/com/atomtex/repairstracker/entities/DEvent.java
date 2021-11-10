package com.atomtex.repairstracker.entities;

import java.util.Date;

public class DEvent {

    private Date date;
    private String state;
    private String location;
    private String id;

    /**Дата добавления события*/
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    /**Статус устройства (На градуировке, поверка, принят в ремонт и т.д.)*/
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**Где находится устройство (Участок РиР, Группа Сервиса и т.д.)*/
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /***/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
