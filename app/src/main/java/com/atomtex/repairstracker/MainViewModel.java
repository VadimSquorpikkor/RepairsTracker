package com.atomtex.repairstracker;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {

    private final FireDBHelper dbh;
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

    public void showEvents(int position, FragmentManager fragmentManager) {
//        if (unitListToObserve.getValue()==null||selectedUnit.getValue()==null)return;
        selectedUnit.setValue(unitListToObserve.getValue().get(position));
        dbh.getEventsFromDB(selectedUnit.getValue().getId(), eventsForSelectedUnit);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}
