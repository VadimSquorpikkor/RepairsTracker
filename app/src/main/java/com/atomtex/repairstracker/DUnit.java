package com.atomtex.repairstracker;

import java.util.Date;

import static com.atomtex.repairstracker.Constant.REPAIR_TYPE;
import static com.atomtex.repairstracker.Constant.SERIAL_TYPE;

public class DUnit {

    //todo сейчас последнее событие сохраняется в юните, что не правильно, надо хранить все события
    // в событиях и только. При чтении юнита нужно загружать из коллекции событий последнее.
    // Поэтому нужно сделать загрузку последнего события и убрать из юнита поля: state, description, location, date

    private String id; //"0001"
    private String name; //БДКГ-02
    private String innerSerial; //№12345
    private String serial; //132.002
    private String type; //"Ремонтный"
    private String state; //"На линейке"
    private String description;
    private String location;
    private String employee; //Фамилия ответственного

    private Date date;
    private Date closeDate;


    public DUnit() {
    }

    public DUnit(String id, String name, String innerSerial, String serial, String state, String description, String type, String location) {
        this.id = id;
        this.name = name;
        this.innerSerial = innerSerial;
        this.serial = serial;
        this.type = type;
        this.state = state;
        this.description = description;
        this.location = location;
    }

    public DUnit(String id, String name, String innerSerial, String serial, String state, String description, String type, String location, Date date) {
        this.id = id;
        this.name = name;
        this.innerSerial = innerSerial;
        this.serial = serial;
        this.type = type;
        this.state = state;
        this.description = description;
        this.location = location;
        this.date = date;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    //Если юнит завершен, то у него появляется дата закрытия
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

    /**Внутренний номер устройства*/
    public String getInnerSerial() {
        return innerSerial;
    }

    public void setInnerSerial(String serial) {
        this.innerSerial = serial;
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

    /**Тип устройства: серийный или ремонтный*/
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**Описание (необязательный параметр)*/
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**Имя последней локации (местонахождения) устройства*/
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**Фамилия ответственного*/
    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
