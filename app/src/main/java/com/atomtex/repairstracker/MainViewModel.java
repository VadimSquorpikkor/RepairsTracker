package com.atomtex.repairstracker;

import android.app.Application;

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

public class MainViewModel extends AndroidViewModel {

    private final FireDBHelper dbh;
//--------------------------------------------------------------------------------------------------
    private final MutableLiveData<ArrayList<DUnit>> unitListToObserve;

    private final MutableLiveData<ArrayList<String>> allStatesIdList;
    private final MutableLiveData<ArrayList<String>> allStatesNameList;
    private final MutableLiveData<ArrayList<String>> deviceIdList;
    private final MutableLiveData<ArrayList<String>> deviceNameList;
    private final MutableLiveData<ArrayList<String>> repairStateIdList;
    private final MutableLiveData<ArrayList<String>> repairStatesNames;
    private final MutableLiveData<ArrayList<String>> locationIdList;
    private final MutableLiveData<ArrayList<String>> locationNamesList;

//--------------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<DUnit>> getUnitListToObserve() {
        return unitListToObserve;
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
        unitListToObserve = new MutableLiveData<>();
        allStatesIdList = new MutableLiveData<>();
        allStatesNameList = new MutableLiveData<>();
        deviceIdList = new MutableLiveData<>();
        deviceNameList = new MutableLiveData<>();
        repairStateIdList = new MutableLiveData<>();
        repairStatesNames = new MutableLiveData<>();
        locationIdList = new MutableLiveData<>();
        locationNamesList = new MutableLiveData<>();

        initializeObserveList();

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

//--------------------------------------------------------------------------------------------------

    void initializeObserveList() {
        ArrayList<DUnit> list = new ArrayList<>();
        unitListToObserve.setValue(list);
    }

    public void openAddDeviceDialog(FragmentManager fragmentManager) {
        SearchUnitParamsDialog dialog = new SearchUnitParamsDialog();
        dialog.show(fragmentManager, null);
    }

    /**
     * Метод получает серийный номер юнита и проверяет наличие в БД. Если такой есть, то добавляет
     * юнит в коллекцию отслеживаемых программой юнитов */
    public void addUnitToObserveCollection(String serial) {
        dbh.getUnitBySerialAndAddToList(unitListToObserve, serial);
    }

}
