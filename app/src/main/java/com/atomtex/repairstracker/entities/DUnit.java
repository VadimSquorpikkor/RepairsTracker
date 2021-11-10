package com.atomtex.repairstracker.entities;

import java.util.Date;

public class DUnit{

    private String id; //"0001"
    private String name; //БДКГ-02
    private String serial; //132.002
    private String state; //поверка, принят в ремонт и т.д.
    private String location;
    private Date date;
    private Date closeDate;
    private String trackId;

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    /**Возвращает true, если ремонт для текущего устройства завершен. Если юнит завершен, то у него появляется дата закрытия*/
    public boolean isComplete() {
        return this.closeDate!=null;
    }

    /**Сколько дней юнит находится в серии/ремонте. Если юнит не закрыт, то считается количество
     * дней от начала ремонта/серии до сегодняшнего дня, если юнит закрыт — до дня закрытия*/
    public int daysPassed() {
        if (this.date==null) return 0;
        Date end = closeDate==null?new Date():closeDate;
        return (int)((end.getTime()-this.date.getTime())/(1000*60*60*24));
    }

    /**Имя последнего статуса устройства*/
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    /**Имя устройства (БДКГ-02)*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**Серийный номер устройства*/
    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**Идентификатор устройства (для серии - AT6130_123, для ремонтных - r_0005)*/
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**Имя последней локации (местонахождения) устройства*/
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

}
