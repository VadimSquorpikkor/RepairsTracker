package com.atomtex.repairstracker;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import static com.atomtex.repairstracker.SaveLoad.loadStringArray;
import static com.atomtex.repairstracker.SaveLoad.saveArray;

public class MainViewModel extends AndroidViewModel {

    private final FireDBHelper dbh;
    public static final String SERIAL_LIST = "serial_list";
//--------------------------------------------------------------------------------------------------
    private final MutableLiveData<ArrayList<DUnit>> unitListToObserve;
    private final MutableLiveData<DUnit> selectedUnit;
    private final MutableLiveData<ArrayList<DEvent>> eventsForSelectedUnit;
//--------------------------------------------------------------------------------------------------
    public MutableLiveData<ArrayList<DUnit>> getUnitListToObserve() {
        return unitListToObserve;
    }

    public MutableLiveData<DUnit> getSelectedUnit() {
        return selectedUnit;
    }

    public MutableLiveData<ArrayList<DEvent>> getEventsForSelectedUnit() {
        return eventsForSelectedUnit;
    }
//--------------------------------------------------------------------------------------------------
    public MainViewModel(@NonNull @NotNull Application application) {
        super(application);
        dbh = new FireDBHelper();
        unitListToObserve = new MutableLiveData<>();
        selectedUnit = new MutableLiveData<>();
        eventsForSelectedUnit = new MutableLiveData<>();
        initializeObserveList();
    }
//--------------------------------------------------------------------------------------------------
    /**При загрузке приложения загружает сохраненный ранее список серийных номеров. Если список
     * не пустой, то загружает из БД устройства по серийникам из этого списка. Т.е. при старте
     * программы отображается список отслеживаемых устройств, добавленных ранее*/
    void initializeObserveList() {
        ArrayList<DUnit> list = new ArrayList<>();
        unitListToObserve.setValue(list);
        ArrayList<String> serials = loadStringArray(SERIAL_LIST);
        for (String s:serials) {
            addUnitToObserveCollection(s);
        }
    }

    /**
     * Метод получает серийный номер юнита и проверяет наличие в БД. Если такой есть, то добавляет
     * юнит в коллекцию отслеживаемых программой юнитов */
    public void addUnitToObserveCollection(String serial) {
        dbh.getUnitBySerialAndAddToList(unitListToObserve, serial);
    }

    /**Загружает из БД список событий для выбранного устройства*/
    public void showEvents(int position, FragmentManager fragmentManager) {
        selectedUnit.setValue(unitListToObserve.getValue().get(position));
        dbh.getEventsFromDB(selectedUnit.getValue().getId(), eventsForSelectedUnit);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    /**При успешном добавлении в список отлеживаемых устройств нового устройства сохраняет в
     * SharedPreferences список серийных номеров этих устройств. Будет необходим для восстановления
     * списка после перезагрузке приложения*/
    void updateUnitsSerialNumbersList() {
        if (unitListToObserve.getValue()==null)return;
        ArrayList<String> serialList = new ArrayList<>();//обнуление
        String s;
        for (DUnit u:unitListToObserve.getValue()) {
            s = u.getSerial();
            if (!serialList.contains(s)) serialList.add(s);
        }
        saveArray(SERIAL_LIST, serialList);
    }
}
