package com.atomtex.repairstracker;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.atomtex.repairstracker.entities.DEvent;
import com.atomtex.repairstracker.entities.DUnit;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static com.atomtex.repairstracker.utils.Constant.EVENT_DATE;
import static com.atomtex.repairstracker.utils.Constant.EVENT_LOCATION;
import static com.atomtex.repairstracker.utils.Constant.EVENT_STATE;
import static com.atomtex.repairstracker.utils.Constant.EVENT_UNIT;
import static com.atomtex.repairstracker.utils.Constant.TABLE_EVENTS;
import static com.atomtex.repairstracker.utils.Constant.TABLE_NAMES;
import static com.atomtex.repairstracker.utils.Constant.TABLE_UNITS;
import static com.atomtex.repairstracker.utils.Constant.TAG;
import static com.atomtex.repairstracker.utils.Constant.UNIT_CLOSE_DATE;
import static com.atomtex.repairstracker.utils.Constant.UNIT_DATE;
import static com.atomtex.repairstracker.utils.Constant.UNIT_DEVICE;
import static com.atomtex.repairstracker.utils.Constant.UNIT_ID;
import static com.atomtex.repairstracker.utils.Constant.UNIT_LOCATION;
import static com.atomtex.repairstracker.utils.Constant.UNIT_SERIAL;
import static com.atomtex.repairstracker.utils.Constant.UNIT_STATE;
import static com.atomtex.repairstracker.utils.Constant.UNIT_TRACK_ID;
import static com.atomtex.repairstracker.utils.Utils.isEmptyOrNull;

class FireDBHelper {

    private final FirebaseFirestore db;

    public FireDBHelper() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Метол получает trackId устройства, находит в БД устройство (может быть и не одно) с
     * таким trackId и добавляет в MutableLiveData<ArrayList<DUnit>> unitList список найденных
     * устройств. На unitList подписан RecyclerView, при изменении в unitList сразу же автоматом
     * в UI формируется список найденных устройств: добавленные ранее + только что найденные
     * <p>
     * <p>
     * Следует понимать: юнит хранит "device_id", который является идентификатором устройства,
     * И ОН ЖЕ ЯВЛЯЕТСЯ ИДЕНТИФИКАТОРОМ ИМЕНИ УСТРОЙСТВА, т.е. например у юнита есть device_id="АТ6130"
     * значит чтобы получить имя этого устройства на русском языке не нужно получать из БД само устройство
     * (это тоже сработает, но это лишнее действие), а сразу из таблицы имен получить объект имен с
     * идентификатором АТ6130 и у него получить имя по полю "ru" (names->AT6130->ru)
     * <p>
     * <p>
     * Ещё одна мулька: в Firestore нет JOIN (потому как нерелеационная БД), поэтому сделан руками:
     * сразу загружаем список юнитов, а потом по уже загруженному списку юнитов делаю запросы в БД,
     * чтобы заменить идентификаторы на имена на выбранном языке: "rep_r_prinyat" ->  "Accepted for repair"
     * Надо помнить, что хоть код //JOIN по коду выполняется сразу после присваивания unit.set...,
     * на самом деле он выполняется уже после того, как присвоились значения, объект unit добавлен
     * в unitList, а сам unitList помещен в MutableLiveData
     *
     * И загрузка и обновление теперь загружает данные из БД по trackId, при этом каждый новый
     * элемент проверяется по id, есть ли в списке отслеживаемых устройств элемент с таким id: если
     * нет — добавляется, если есть — обновляются данные. По факту: если данные в отслеживаемых
     * устройствах изменятся, при старте/обновлении изменения отобразятся; если было добавлено ещё
     * устройство с таким же trackId (например в ремонте комплект устройств, у них общий trackId),
     * оно добавится в список; если удалить из списка устройство из комплекта, при старте/обновлении
     * оно опять отобразится в списке
     */
    void getUnitByTrackIdAndAddToList(MutableLiveData<ArrayList<DUnit>> unitList, String trackId) {
        Query query = db.collection(TABLE_UNITS).whereEqualTo(UNIT_TRACK_ID, trackId);

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) //noinspection CommentedOutCode
                    {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null) return;
                        if (unitList == null) return;
                        ArrayList<DUnit> list = new ArrayList<>();
                        if (unitList.getValue() != null) list.addAll(unitList.getValue());
                        for (DocumentSnapshot document : task.getResult()) {
                            DUnit unit = new DUnit();
                            unit.setName(String.valueOf(document.get(UNIT_DEVICE)));
                            unit.setId(String.valueOf(document.get(UNIT_ID)));
                            unit.setLocation(String.valueOf(document.get(UNIT_LOCATION)));
                            unit.setSerial(String.valueOf(document.get(UNIT_SERIAL)));
                            unit.setState(String.valueOf(document.get(UNIT_STATE)));
                            unit.setTrackId(String.valueOf(document.get(UNIT_TRACK_ID)));
                            Timestamp timestamp = (Timestamp) document.get(UNIT_DATE);
                            if (timestamp != null) unit.setDate(timestamp.toDate());
                            Timestamp closeTimestamp = (Timestamp) document.get(UNIT_CLOSE_DATE);
                            if (closeTimestamp != null) unit.setCloseDate(closeTimestamp.toDate());

                            //JOIN------------------------------------------------------------------
                            String state = String.valueOf(document.get(UNIT_STATE));
                            if (!isEmptyOrNull(state)) {
                                db.collection(TABLE_NAMES).document(state).get()
                                        .addOnCompleteListener(task1 -> {
                                            unit.setState(getStringFromSnapshot(task1, state));
                                            updateLiveData(unitList);
                                        });
                            }
                            String location = String.valueOf(document.get(UNIT_LOCATION));
                            if (!isEmptyOrNull(location)) {
                                db.collection(TABLE_NAMES).document(location).get()
                                        .addOnCompleteListener(task2 -> {
                                            unit.setLocation(getStringFromSnapshot(task2, location));
                                            updateLiveData(unitList);
                                        });
                            }
                            String name = String.valueOf(document.get(UNIT_DEVICE));
                            if (!isEmptyOrNull(name)) {
                                db.collection(TABLE_NAMES).document(name).get()
                                        .addOnCompleteListener(task3 -> {
                                            unit.setName(getStringFromSnapshot(task3, name));
                                            updateLiveData(unitList);
                                        });
                            }
                            //----------------------------------------------------------------------

                            int position = idInListPosition(unitList.getValue(), unit.getId());
                            if (position != -1) {
                                Log.e(TAG, "position: "+position+" serial: " + unit.getSerial());
                                list.set(position, unit);//обновить, если такой юнит уже есть в списке
                            } else list.add(unit);//а если нет, то добавить
                        }

                        //noinspection ComparatorCombinators
                        Collections.sort(list, (o1, o2) -> o1.getTrackId().compareTo(o2.getTrackId()));
//                        list.sort((o1, o2) -> o1.getTrackId().compareTo(o2.getTrackId()));
//                        Collections.sort(list);

                        unitList.setValue(list);
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    /**
     * Для перевода в нужный язык. Загружает из таблицы слово на всех языках и выбирает значение
     * для языка, выбранного в телефоне (lang) и значение на английском. Если слово на языке телефона
     * есть, то возвращаем его, если нет или равен "", то антлийское, если нет и английского, то значение по
     * умолчанию (идентификатор)
     */
    String getStringFromSnapshot(Task<DocumentSnapshot> task, String defValue) {
        if (task.isSuccessful()) {
            DocumentSnapshot documentSnapshot = task.getResult();
            String lang = Locale.getDefault().getLanguage();
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Object o = documentSnapshot.get(lang);
                Object def = documentSnapshot.get("en");
                if (o != null && !o.toString().equals("")) {
                    return o.toString();
                }
                //если нет языка телефона в БД и не равно "", то присваивается английский вариант
                else if (def != null && !def.toString().equals("")) {
                    Log.e(TAG, "нет такого языка!");
                    return def.toString();
                }
                //если и английского нет в БД и не равно "", то оставляется идентификатор. он и будет отображаться в
                else {
                    Log.e(TAG, "и английского нет!");
                    return defValue;
                }
            } else {
                Log.e(TAG, "нет такого id!");
                return defValue;
            }
        }
        return defValue;
    }

    /**
     * Тыркает MutableLiveData, чтобы обновил UI
     */
    void updateLiveData(MutableLiveData<ArrayList<DUnit>> data) {
        data.setValue(data.getValue());
    }

    /**
     * Тыркает MutableLiveData, чтобы обновил UI
     */
    void updateLiveData2(MutableLiveData<ArrayList<DEvent>> data) {
        data.setValue(data.getValue());
    }

    /**
     * Загружает список событий по "unit_id", т.е. для конкретного устройства
     */
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
                    DEvent event = new DEvent();
                    Timestamp timestamp = (Timestamp) q.get(UNIT_DATE);
                    if (timestamp != null) event.setDate(timestamp.toDate());
                    event.setState(String.valueOf(q.get(EVENT_STATE)));
                    event.setLocation(String.valueOf(q.get(EVENT_LOCATION)));

                    //JOIN
                    String state = String.valueOf(q.get(EVENT_STATE));
                    if (!isEmptyOrNull(state)) {
                        db.collection(TABLE_NAMES).document(state).get()
                                .addOnCompleteListener(task1 -> {
                                    event.setState(getStringFromSnapshot(task1, state));
                                    updateLiveData2(events);
                                });
                    }
                    String location = String.valueOf(q.get(EVENT_LOCATION));
                    if (!isEmptyOrNull(location)) {
                        db.collection(TABLE_NAMES).document(location).get()
                                .addOnCompleteListener(task2 -> {
                                    event.setLocation(getStringFromSnapshot(task2, location));
                                    updateLiveData2(events);
                                });
                    }

                    newEvents.add(event);
                }
                events.setValue(newEvents);
            } else {
                Log.e(TAG, "Error - " + task.getException());
            }
        });
    }

    /**
     * Если юнит с таким id уже есть в списке, то метод возвращает его позицию в списке, иначе возвращает -1
     */
    private int idInListPosition(ArrayList<DUnit> list, String id) {
        if (list == null || list.size() == 0) return -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) return i;
        }
        return -1;
    }
}
