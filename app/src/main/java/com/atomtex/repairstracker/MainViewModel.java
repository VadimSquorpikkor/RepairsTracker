package com.atomtex.repairstracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.jetbrains.annotations.NotNull;

public class MainViewModel extends AndroidViewModel {

    public static final String TAG = "tag";

    public static final String TABLE_UNITS = "units";
    public static final String UNIT_DATE = "date";
    public static final String UNIT_DESCRIPTION = "description";
    public static final String UNIT_DEVICE = "device_id"; //todo возможно в имени стринга и не нужен "_ID", только в значении
    public static final String UNIT_EMPLOYEE = "employee_id";
    public static final String UNIT_ID = "id";
    public static final String UNIT_INNER_SERIAL = "inner_serial";
    public static final String UNIT_LOCATION = "location_id";
    public static final String UNIT_SERIAL = "serial";
    public static final String UNIT_STATE = "state_id";
    public static final String UNIT_TYPE = "type_id";//todo по-хорошему нужна коллекция тайпов. Пока обойдусь

    public static final String TABLE_STATES = "states"; //в прошлом profile
    public static final String STATE_ID = "id";
    public static final String STATE_LOCATION = "location_id";
    public static final String STATE_NAME = "name";
    public static final String STATE_TYPE = "type_id";

    public static final String TABLE_EVENTS = "events"; //в прошлом states
    public static final String EVENT_DATE = "date";
    public static final String EVENT_CLOSE_DATE = "close_date";
    public static final String EVENT_DESCRIPTION = "description";
    public static final String EVENT_LOCATION = "location_id";
    public static final String EVENT_STATE = "state_id";
    public static final String EVENT_UNIT = "unit_id";

    public static final String TABLE_EMPLOYEES = "employees"; //в прошлом users
    public static final String EMPLOYEE_EMAIL = "email"; //email нельзя использовать в качестве id, так как у пользователя может поменяться email, и тогда при необходимости выбрать устройства пользователя нужно будет искать и по старому email и по новому
    public static final String EMPLOYEE_ID = "id";
    public static final String EMPLOYEE_LOCATION = "location_id";
    public static final String EMPLOYEE_NAME = "name";

    public static final String TABLE_LOCATIONS = "locations";
    public static final String LOCATION_ID = "id";
    public static final String LOCATION_NAME = "name";

    public static final String TABLE_DEVICES = "devices";
    public static final String DEVICE_ID = "id";
    public static final String DEVICE_NAME = "name";
    public static final String DEVICE_TYPE = "type";

    public static final String TYPE_ANY = "any_type";
    public static final String TYPE_REPAIR = "repair_type";
    public static final String TYPE_SERIAL = "serial_type";

    public static final String EMPTY_LOCATION_ID = "empty_location_id";
    public static final String EMPTY_LOCATION_NAME = "Локация не найдена";
    //--------------------------------------------------------------------------------------------------
    public static final String SERIAL_TYPE = "serial_type";
    public static final String REPAIR_TYPE = "repair_type";

    private static final String SPLIT_SYMBOL = " ";
    public static final String REPAIR_UNIT = "Ремонт";
    public static final String SERIAL_UNIT = "Серия";
    public static final String ANY_VALUE = "any_value";
    public static final String ANY_VALUE_TEXT = "- любой -";//"- любой -"
    public static final String EMPTY_VALUE_TEXT = "- не выбран -";//"- не выбран -"
    public static final String EXTRA_POSITION = "position";

    private final FireDBHelper dbh;

    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);

        dbh = new FireDBHelper();
    }


}
