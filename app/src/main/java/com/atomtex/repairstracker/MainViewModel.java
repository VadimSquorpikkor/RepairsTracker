package com.atomtex.repairstracker;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

import static com.atomtex.repairstracker.Constant.DEVICE_ID;
import static com.atomtex.repairstracker.Constant.DEVICE_NAME;
import static com.atomtex.repairstracker.Constant.STATE_ID;
import static com.atomtex.repairstracker.Constant.STATE_NAME;
import static com.atomtex.repairstracker.Constant.TABLE_DEVICES;
import static com.atomtex.repairstracker.Constant.TABLE_STATES;
import static com.atomtex.repairstracker.Constant.TAG;
import static com.atomtex.repairstracker.Constant.UNIT_DEVICE;
import static com.atomtex.repairstracker.Constant.UNIT_EMPLOYEE;
import static com.atomtex.repairstracker.Constant.UNIT_LOCATION;
import static com.atomtex.repairstracker.Constant.UNIT_SERIAL;
import static com.atomtex.repairstracker.Constant.UNIT_STATE;
import static com.atomtex.repairstracker.Constant.UNIT_TYPE;

public class MainViewModel extends AndroidViewModel {

    private final FireDBHelper dbh;
//--------------------------------------------------------------------------------------------------
    private final MutableLiveData<ArrayList<DUnit>> repairUnitList;

    private final MutableLiveData<ArrayList<String>> allStatesIdList;
    private final MutableLiveData<ArrayList<String>> allStatesNameList;
    private final MutableLiveData<ArrayList<String>> deviceIdList;
    private final MutableLiveData<ArrayList<String>> deviceNameList;
    private final MutableLiveData<ArrayList<String>> repairStateIdList;
    private final MutableLiveData<ArrayList<String>> repairStatesNames;
    private final MutableLiveData<ArrayList<String>> locationIdList;
    private final MutableLiveData<ArrayList<String>> locationNamesList;

//--------------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<DUnit>> getRepairUnitList() {
        return repairUnitList;
    }

    public MutableLiveData<ArrayList<String>> getAllStatesIdList() {
        return allStatesIdList;
    }

    public MutableLiveData<ArrayList<String>> getAllStatesNameList() {
        return allStatesNameList;
    }

    public MutableLiveData<ArrayList<String>> getDeviceIdList() {
        return deviceIdList;
    }

    public MutableLiveData<ArrayList<String>> getDeviceNameList() {
        return deviceNameList;
    }

    public MutableLiveData<ArrayList<String>> getRepairStateIdList() {
        return repairStateIdList;
    }

    public MutableLiveData<ArrayList<String>> getRepairStatesNames() {
        return repairStatesNames;
    }

    public MutableLiveData<ArrayList<String>> getLocationIdList() {
        return locationIdList;
    }

    public MutableLiveData<ArrayList<String>> getLocationNamesList() {
        return locationNamesList;
    }

//--------------------------------------------------------------------------------------------------
    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
        dbh = new FireDBHelper();
        repairUnitList = new MutableLiveData<>();
        allStatesIdList = new MutableLiveData<>();
        allStatesNameList = new MutableLiveData<>();
        deviceIdList = new MutableLiveData<>();
        deviceNameList = new MutableLiveData<>();
        repairStateIdList = new MutableLiveData<>();
        repairStatesNames = new MutableLiveData<>();
        locationIdList = new MutableLiveData<>();
        locationNamesList = new MutableLiveData<>();

        addAllStatesListener();
        addDeviceNameListener();
        addDeviceIdListener();
    }
//----- LISTENERS ----------------------------------------------------------------------------------

    //TODO !!! Это НЕ лисенер, метод просто загружает данные, но не отслеживает их изменения!!!
    // Переделать под лисенер
    void addAllStatesListener() {
        dbh.getListIdsAndListNames(TABLE_STATES, allStatesIdList, STATE_ID, allStatesNameList, STATE_NAME);
    }

    //todo ВСЕ парные лисенеры (Name / Id) переделать в один лисенер, который будет отслеживать одну таблицу, а заполнять изменения в два MutableLiveData.
    /**
     * Слушатель для таблицы имен приборов
     */
    void addDeviceNameListener() {
        dbh.addStringArrayListener(TABLE_DEVICES, deviceNameList, DEVICE_NAME);
    }

    void addDeviceIdListener() {
        dbh.addStringArrayListener(TABLE_DEVICES, deviceIdList, DEVICE_ID);
    }




    /**По выбранным параметрам получает из БД список юнитов*/
    public void getUnitListFromBD(String deviceName, String location, String employee, String type, String state, String serial) {
        Log.e(TAG, "♦ deviceName - "+deviceName+" location - "+location+" employee - "+employee+" type - "+type);
        dbh.getUnitListByParam(repairUnitList, UNIT_DEVICE, deviceName, UNIT_LOCATION, location, UNIT_EMPLOYEE, employee, UNIT_TYPE, type, UNIT_STATE, state, UNIT_SERIAL, serial);
    }

    public void openAddDeviceDialog(FragmentManager fragmentManager) {
        SearchUnitParamsDialog dialog = new SearchUnitParamsDialog();
        dialog.show(fragmentManager, null);
    }
}
