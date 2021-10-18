package com.atomtex.repairstracker;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

import static com.atomtex.repairstracker.utils.Constant.TAG;
import static com.atomtex.repairstracker.utils.SaveLoad.loadStringArray;
import static com.atomtex.repairstracker.utils.SaveLoad.saveArray;

import com.atomtex.repairstracker.entities.DEvent;
import com.atomtex.repairstracker.entities.DUnit;
import com.atomtex.repairstracker.fragments.InfoFragment;

public class MainViewModel extends AndroidViewModel {

    private final FireDBHelper dbh;
    public static final String TRACK_ID_LIST = "track_id_list";
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
    /**При загрузке приложения загружает сохраненный ранее список trackId номеров. Если список
     * не пустой, то загружает из БД устройства по серийникам из этого списка. Т.е. при старте
     * программы отображается список отслеживаемых устройств, добавленных ранее*/
    public void initializeObserveList() {
        Log.e(TAG, "****************initializeObserveList********************");
        //получаем сохраненный ранее в преференсах список trackId номеров
        ArrayList<String> trackIds = loadStringArray(TRACK_ID_LIST);
        //////unitListToObserve.setValue(new ArrayList<>());////////////////////////////////////////////////////////////////////
        //по списку trackId загружаем из БД устройства
        for (String s:trackIds) {
            Log.e(TAG, "****************initializeObserveList: "+s);
            addUnitToObserveCollection(s);
        }
    }

    /**
     * Метод получает серийный номер юнита и проверяет наличие в БД. Если такой есть, то добавляет
     * юнит в коллекцию отслеживаемых программой юнитов */
    public void addUnitToObserveCollection(String trackId) {
        dbh.getUnitByTrackIdAndAddToList(unitListToObserve, trackId);
    }

    /**Загружает из БД список событий для выбранного устройства*/
    public void showEvents(int position, FragmentManager fragmentManager) {
        if (unitListToObserve.getValue()==null) return;
        selectedUnit.setValue(unitListToObserve.getValue().get(position));
        dbh.getEventsFromDB(selectedUnit.getValue().getId(), eventsForSelectedUnit);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, InfoFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    /**При успешном добавлении в список отлеживаемых устройств нового устройства сохраняет в
     * SharedPreferences список trackId этих устройств. Будет необходим для восстановления
     * списка после перезагрузке приложения*/
    public void updateUnitsTrackIdNumbersList() {
        Log.e(TAG, "****************updateUnitsTrackIdNumbersList****************");
        if (unitListToObserve.getValue()==null)return;
        ArrayList<String> trackIdList = new ArrayList<>();//обнуление
        String s;
        for (DUnit u:unitListToObserve.getValue()) {
            s = u.getTrackId();
            if (!trackIdList.contains(s)) trackIdList.add(s);
            Log.e(TAG, "****************updateUnitsTrackIdNumbersList: "+s);
        }
        saveArray(TRACK_ID_LIST, trackIdList);
    }

    /**Удаляет устройство из списка и сохраняет получившийся список*/
    public void removeItemFromList(int position) {
        unitListToObserve.getValue().remove(position);
        unitListToObserve.setValue(unitListToObserve.getValue());//update
        updateUnitsTrackIdNumbersList();
    }

    public void refresh() {
        if (unitListToObserve==null || unitListToObserve.getValue()==null) return;
        for (DUnit unit:unitListToObserve.getValue()) {
            dbh.getUnitByIdAndAddToList(unitListToObserve, unit.getId());
        }
    }
}
