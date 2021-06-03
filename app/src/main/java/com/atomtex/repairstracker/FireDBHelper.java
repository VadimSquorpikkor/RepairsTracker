package com.atomtex.repairstracker;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;
import static com.atomtex.repairstracker.Constant.EVENT_DATE;
import static com.atomtex.repairstracker.Constant.EVENT_LOCATION;
import static com.atomtex.repairstracker.Constant.EVENT_STATE;
import static com.atomtex.repairstracker.Constant.EVENT_UNIT;
import static com.atomtex.repairstracker.Constant.TABLE_EVENTS;
import static com.atomtex.repairstracker.Constant.TABLE_UNITS;
import static com.atomtex.repairstracker.Constant.TAG;
import static com.atomtex.repairstracker.Constant.UNIT_DATE;
import static com.atomtex.repairstracker.Constant.UNIT_DEVICE;
import static com.atomtex.repairstracker.Constant.UNIT_ID;
import static com.atomtex.repairstracker.Constant.UNIT_LOCATION;
import static com.atomtex.repairstracker.Constant.UNIT_SERIAL;
import static com.atomtex.repairstracker.Constant.UNIT_STATE;

class FireDBHelper {

    private final FirebaseFirestore db;

    public FireDBHelper() {
        db = FirebaseFirestore.getInstance();
    }

    /**Метол получает серийный номер устройства, находит в БД устройство (может быть и не одно) с
     * таким серийником и добавляет в MutableLiveData<ArrayList<DUnit>> unitList список найденных
     * устройств. На unitList подписан RecyclerView, при изменении в unitList сразу же автоматом
     * в UI формируется список найденных устройств: добавленные ранее + только что найденные */
    void getUnitBySerialAndAddToList(MutableLiveData<ArrayList<DUnit>> unitList, String serial) {
        Query query = db.collection(TABLE_UNITS).whereEqualTo(UNIT_SERIAL, serial);

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null) return;
                        if (unitList==null||unitList.getValue()==null)return;
                        ArrayList<DUnit> list = new ArrayList<>(unitList.getValue());
                        for (DocumentSnapshot document : task.getResult()) {
                            DUnit unit = new DUnit();
                            unit.setName(String.valueOf(document.get(UNIT_DEVICE)));
                            unit.setId(String.valueOf(document.get(UNIT_ID)));
                            unit.setLocation(String.valueOf(document.get(UNIT_LOCATION)));
                            unit.setSerial(String.valueOf(document.get(UNIT_SERIAL)));
                            unit.setState(String.valueOf(document.get(UNIT_STATE)));
                            Timestamp timestamp = (Timestamp) document.get(UNIT_DATE);
                            if (timestamp!=null)unit.setDate(timestamp.toDate());
                            list.add(unit);
                        }
                        unitList.setValue(list);

                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    /**Загружает список событий по "unit_id", т.е. для конкретного устройства*/
    void getEventsFromDB(String unit_id, MutableLiveData<ArrayList<DEvent>> events) {
        db.collection(TABLE_EVENTS)
                .whereEqualTo(EVENT_UNIT, unit_id)
                .orderBy(EVENT_DATE, Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null) return;
                        ArrayList<DEvent> newEvents = new ArrayList<>();
                        for (QueryDocumentSnapshot q : querySnapshot) {
                            Timestamp timestamp = (Timestamp) q.get(EVENT_DATE);
                            Date date = timestamp.toDate();
                            String state = q.get(EVENT_STATE).toString();
                            String location = q.get(EVENT_LOCATION).toString();
                            newEvents.add(new DEvent(date, state, location));
                        }
                        events.setValue(newEvents);
                    } else {
                        Log.e(TAG, "Error - " + task.getException());
                    }
                });
    }

}
