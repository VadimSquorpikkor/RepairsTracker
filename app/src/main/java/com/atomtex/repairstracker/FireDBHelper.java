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
import java.util.Arrays;
import java.util.Date;

import static com.atomtex.repairstracker.Constant.ANY_VALUE;
import static com.atomtex.repairstracker.Constant.EVENT_DATE;
import static com.atomtex.repairstracker.Constant.EVENT_DESCRIPTION;
import static com.atomtex.repairstracker.Constant.EVENT_LOCATION;
import static com.atomtex.repairstracker.Constant.EVENT_STATE;
import static com.atomtex.repairstracker.Constant.EVENT_UNIT;
import static com.atomtex.repairstracker.Constant.REPAIR_TYPE;
import static com.atomtex.repairstracker.Constant.STATE_ID;
import static com.atomtex.repairstracker.Constant.STATE_LOCATION;
import static com.atomtex.repairstracker.Constant.STATE_NAME;
import static com.atomtex.repairstracker.Constant.STATE_TYPE;
import static com.atomtex.repairstracker.Constant.TABLE_EVENTS;
import static com.atomtex.repairstracker.Constant.TABLE_STATES;
import static com.atomtex.repairstracker.Constant.TABLE_UNITS;
import static com.atomtex.repairstracker.Constant.TAG;
import static com.atomtex.repairstracker.Constant.TYPE_ANY;
import static com.atomtex.repairstracker.Constant.UNIT_DATE;
import static com.atomtex.repairstracker.Constant.UNIT_DESCRIPTION;
import static com.atomtex.repairstracker.Constant.UNIT_DEVICE;
import static com.atomtex.repairstracker.Constant.UNIT_EMPLOYEE;
import static com.atomtex.repairstracker.Constant.UNIT_ID;
import static com.atomtex.repairstracker.Constant.UNIT_INNER_SERIAL;
import static com.atomtex.repairstracker.Constant.UNIT_LOCATION;
import static com.atomtex.repairstracker.Constant.UNIT_SERIAL;
import static com.atomtex.repairstracker.Constant.UNIT_STATE;
import static com.atomtex.repairstracker.Constant.UNIT_TYPE;

class FireDBHelper {

    private final FirebaseFirestore db;

    public FireDBHelper() {
        db = FirebaseFirestore.getInstance();
    }

    @SuppressWarnings("SameParameterValue")
    void addStringArrayListener(String table, MutableLiveData<ArrayList<String>> mList, String fieldName) {
        Log.e(TAG, "addStringArrayListener: ");
        db.collection(table).addSnapshotListener((queryDocumentSnapshots, error) -> {
            //getStringFromDB(table, s);
            getStringArrayFromDB(table, mList, fieldName);
        });
    }

    /**
     * Получаем юнит из БД по его идентификатору
     * @param id id юнита, которого нужно прочитать в БД
     * @param selectedUnit MutableListData, в который записываем найденный юнит
     */
    void getUnitById_EXP(String id, MutableLiveData<DUnit> selectedUnit) {
        db.collection(TABLE_UNITS)
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        //todo НЕ РАБОТАЛО С "if (documentSnapshot == null) return" только с "if (documentSnapshot.exists())", нужно проверить у других методов, как там работает
                        if (documentSnapshot.exists()){
                            DUnit unit = new DUnit();
                            unit.setDescription(String.valueOf(documentSnapshot.get(UNIT_DESCRIPTION)));
                            unit.setName(String.valueOf(documentSnapshot.get(UNIT_DEVICE)));
                            unit.setEmployee(String.valueOf(documentSnapshot.get(UNIT_EMPLOYEE)));
                            unit.setId(String.valueOf(documentSnapshot.get(UNIT_ID)));
                            unit.setInnerSerial(String.valueOf(documentSnapshot.get(UNIT_INNER_SERIAL)));
                            unit.setLocation(String.valueOf(documentSnapshot.get(UNIT_LOCATION)));
                            unit.setSerial(String.valueOf(documentSnapshot.get(UNIT_SERIAL)));
                            unit.setState(String.valueOf(documentSnapshot.get(UNIT_STATE)));
                            unit.setType(String.valueOf(documentSnapshot.get(UNIT_TYPE)));
                            Timestamp timestamp = (Timestamp) documentSnapshot.get(UNIT_DATE);
                            if (timestamp!=null)unit.setDate(timestamp.toDate());
                            selectedUnit.setValue(unit);
                        }
                        else{ Log.e(TAG, "☻ getUnitById_EXP: NOT EXISTS");}
                        ///if (documentSnapshot == null) return;
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

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
                            unit.setDescription(String.valueOf(document.get(UNIT_DESCRIPTION)));
                            unit.setName(String.valueOf(document.get(UNIT_DEVICE)));
                            unit.setEmployee(String.valueOf(document.get(UNIT_EMPLOYEE)));
                            unit.setId(String.valueOf(document.get(UNIT_ID)));
                            unit.setInnerSerial(String.valueOf(document.get(UNIT_INNER_SERIAL)));
                            unit.setLocation(String.valueOf(document.get(UNIT_LOCATION)));
                            unit.setSerial(String.valueOf(document.get(UNIT_SERIAL)));
                            unit.setState(String.valueOf(document.get(UNIT_STATE)));
                            unit.setType(String.valueOf(document.get(UNIT_TYPE)));
                            //getEventsFromDBForUnit(unit);
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

    /**Обертка для getUnitListByParam*/
    void getRepairUnitListBySerial(MutableLiveData<ArrayList<DUnit>> unitList, String serial) {
        getUnitListByParam(unitList, UNIT_DEVICE, ANY_VALUE,
                UNIT_LOCATION, ANY_VALUE,
                UNIT_EMPLOYEE, ANY_VALUE,
                UNIT_TYPE, REPAIR_TYPE,
                UNIT_STATE, ANY_VALUE,
                UNIT_SERIAL, serial);
    }

    //TODO Сделать getUnitListByParam только по серийному номеру, исё равно других вариантов не будет, только лишние параметры

    //TODO !!! УДАЛИТЬ лишние сеттеры !!! (unit.setEmployee(например String.valueOf(document.get(UNIT_EMPLOYEE))); НЕ НУЖЕН, ответственные не будут отображаться в этом приложении!)
    void getUnitListByParam(MutableLiveData<ArrayList<DUnit>> unitList, String param1, String value1, String param2, String value2, String param3, String value3, String param4, String value4, String param5, String value5, String param6, String value6) {
        Query query = db.collection(TABLE_UNITS);
        if (!value1.equals(ANY_VALUE)) query = query.whereEqualTo(param1, value1);
        if (!value2.equals(ANY_VALUE)) query = query.whereEqualTo(param2, value2);
        if (!value3.equals(ANY_VALUE)) query = query.whereEqualTo(param3, value3);
        if (!value4.equals(ANY_VALUE)) query = query.whereEqualTo(param4, value4);
        if (!value5.equals(ANY_VALUE)) query = query.whereEqualTo(param5, value5);
        if (!value6.equals(ANY_VALUE)) query = query.whereEqualTo(param6, value6);

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "task.isSuccessful");
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot == null) return;
                        Log.e(TAG, "querySnapshot NOT null");
                        ArrayList<DUnit> list = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            DUnit unit = new DUnit();
                            unit.setDescription(String.valueOf(document.get(UNIT_DESCRIPTION)));
                            unit.setName(String.valueOf(document.get(UNIT_DEVICE)));
                            unit.setEmployee(String.valueOf(document.get(UNIT_EMPLOYEE)));
                            unit.setId(String.valueOf(document.get(UNIT_ID)));
                            unit.setInnerSerial(String.valueOf(document.get(UNIT_INNER_SERIAL)));
                            unit.setLocation(String.valueOf(document.get(UNIT_LOCATION)));
                            Log.e(TAG, "unit.getLocation: "+unit.getLocation());
                            unit.setSerial(String.valueOf(document.get(UNIT_SERIAL)));
                            unit.setState(String.valueOf(document.get(UNIT_STATE)));
                            unit.setType(String.valueOf(document.get(UNIT_TYPE)));
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



    /**
     * Получаем лист String из БД и помещаем её в MutableLiveDate
     * @param table имя таблицы (коллекции) из которой берем данные
     * @param mList Mutable, в который помещаем найденные стринги
     * @param fieldName поле таблицы, значение которой считываем в лист
     */
    void getStringArrayFromDB(String table, MutableLiveData<ArrayList<String>> mList, String fieldName) {
        db.collection(table).get().addOnCompleteListener(task -> {
            ArrayList<String> list = new ArrayList<>();
            for (DocumentSnapshot document : task.getResult()) {
                if (document.get(fieldName)!=null) list.add(document.get(fieldName).toString());
            }
            mList.setValue(list);
        });
    }

    void getStringArrayFromDBOrdered(String table, MutableLiveData<ArrayList<String>> mList, String fieldName, String orderBy) {
        db.collection(table).orderBy(orderBy, Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            ArrayList<String> list = new ArrayList<>();
            for (DocumentSnapshot document : task.getResult()) {
                if (document.get(fieldName)!=null) list.add(document.get(fieldName).toString());
                Log.e(TAG, "☻☻☻ getStringArrayFromDBOrdered: "+document.get(fieldName).toString());
            }
            mList.setValue(list);
        });
    }

    /**Слушатель для новых событий у выбранного юнита. Слушает всю коллекцию событий и при новом
     * событии загружает те, у которых "unit_id" равен id выбранного юнита. Другими словами
     * обновляет события выбранного юнита, если список событий изменился*/
    void addSelectedUnitStatesListener(String unit_id, MutableLiveData<ArrayList<DEvent>> unitStatesList) {
        db.collection(TABLE_EVENTS).addSnapshotListener((queryDocumentSnapshots, error) -> {
            getEventsFromDB(unit_id, unitStatesList);
        });
    }

    void addSelectedUnitListener(String unit_id, MutableLiveData<DUnit> mUnit) {
        db.collection(TABLE_UNITS).document(unit_id).addSnapshotListener((queryDocumentSnapshots, error) -> {
            getUnitById_EXP(unit_id, mUnit);
        });
    }

    void getLastEventFromDB(String unit_id, DEvent event) {
        db.collection(TABLE_EVENTS)
                .whereEqualTo(EVENT_UNIT, unit_id)
                .orderBy(EVENT_DATE, Query.Direction.DESCENDING)
                .limit(1)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot == null) return;
                if (querySnapshot.getDocuments().size()==0) return;

                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                Timestamp timestamp = (Timestamp) documentSnapshot.get(EVENT_DATE);
                event.setDate(timestamp.toDate());
                event.setState(documentSnapshot.get(EVENT_STATE).toString());
                event.setDescription(documentSnapshot.get(EVENT_DESCRIPTION).toString());
                event.setLocation(documentSnapshot.get(EVENT_LOCATION).toString());
                event.setUnit_id(documentSnapshot.get(EVENT_UNIT).toString());
                event.setId(documentSnapshot.getId());
                Log.e(TAG, "getLastEventFromDB: "+documentSnapshot.getId());
            } else {
                Log.e(TAG, "Error - " + task.getException());
            }
        });
    }

    /**Загружает список событий по "unit_id"*/
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
                            /*Log.e(TAG, "1: "+q.get("date"));
                            Log.e(TAG, "2: "+timestamp.toDate());
                            Log.e(TAG, "2: "+timestamp.toDate());
                            Log.e(TAG, "2: "+getRightDate(timestamp.getSeconds()));
                            Log.e(TAG, "3: "+q.get("state"));*/

                            Date date = timestamp.toDate();
                            String state = q.get(EVENT_STATE).toString();
                            String location = q.get(EVENT_LOCATION).toString();
                            Log.e(TAG, "♦getEventsFromDB: " + location);
                            newEvents.add(new DEvent(date, state, location));
                        }
                        events.setValue(newEvents);
                    } else {
                        Log.e(TAG, "Error - " + task.getException());
                    }
                });
    }


    /**
     * Получение MutableLiveData<ArrayList<String>> из БД по 2-м параметрам
     * @param table коллекция, по которой будет произведен поиск
     * @param mList MutableLiveData в который будет записан найденный лист
     * @param param1 имя параметра 1 (поля), по которому ведется поиск
     * @param value1 значение параметра 1, по которому ведется поиск
     * @param param2 имя параметра 2 (поля), по которому ведется поиск
     * @param value2 значение параметра 2, по которому ведется поиск
     * @param fieldName это то поле, значение которого будет считываться в возвращаемый ArrayList<String>
     */
    void getStringArrayByParam(String table, MutableLiveData<ArrayList<String>> mList, String param1, String value1, String param2, String value2, String fieldName) {
        db.collection(table)
                .whereEqualTo(param1, value1)
                .whereEqualTo(param2, value2)
                .get().addOnCompleteListener(task -> {
            ArrayList<String> list = new ArrayList<>();
            for (DocumentSnapshot document : task.getResult()) {
                list.add(document.get(fieldName).toString());
            }
            mList.setValue(list);
        });
    }

    /**
     * Поиск по БД по параметру документа и запись результата поиска в MutableLiveData<String>
     * @param table коллекция, по которой будет произведен поиск
     * @param byParamName имя параметра (поля), по которому ведется поиск
     * @param byValueName значение параметра, по которому ведется поиск
     * @param valueToGet имя поля в найденном документе, значение которого будет записано в результат
     * @param outPutString в эту переменную будет записан найденное значение
     * @param stringIfNull если ничего не найдено, это будет записано в outPutString
     */
    void getStringValueByParam(String table, String byParamName, String byValueName, String valueToGet, MutableLiveData<String> outPutString, String stringIfNull) {
        db.collection(table)
                .whereEqualTo(byParamName, byValueName)
                .get().addOnCompleteListener(task -> {
            ArrayList<String> list = new ArrayList<>();
            for (DocumentSnapshot document : task.getResult()) {
                list.add(document.get(valueToGet).toString());
            }
            if (list.size()==0) outPutString.setValue(stringIfNull);
            else outPutString.setValue(list.get(0));
        });
    }

    void getString(String table, String documentId, String field, /*String changingValue*/MutableLiveData<String> changingValue) {
        db.collection(table)
                .document(documentId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            Object o = documentSnapshot.get(field);
                            String value = o==null?"":o.toString();
                            changingValue.setValue(value);
                        } else {
                            Log.e(TAG, "☻ NOT EXISTS");
                        }
                    } else {
                        Log.e(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    void getListIdsAndListNames(String table, MutableLiveData<ArrayList<String>> idList, String idField, MutableLiveData<ArrayList<String>> nameList, String nameField) {
        db.collection(table).get().addOnCompleteListener(task -> {
            ArrayList<String> list1 = new ArrayList<>();
            ArrayList<String> list2 = new ArrayList<>();
            int count = 0;
            for (DocumentSnapshot document : task.getResult()) {
                String id = document.get(idField).toString();
                String name = document.get(nameField).toString();
                list1.add(id);
                list2.add(name);
                //todo если буду делать локализацию, то здесь надо будет вставлять что-то типа if(lang.isEng)name = "name_eng". В БД будет дополнительное поле "name_eng", оно будет выбираться вместо "name". И всё, весь остальной код уже будет работать. Это конечно касается только имени статуса, для других сделать аналогично
            }
            idList.setValue(list1);
            nameList.setValue(list2);
        });
    }

    /**Загружает список статусов по типу (серия/ремонт) и локации (регулировка/монтаж...). Так как
     * есть статусы, которые одинаковые и для ремонта и для серии (у этих статусов тип "any"), то
     * при выборе статусов ищется тип выбранный в параметре метода (ремонт или серия) ИЛИ тип "any"
     * (т.е. при любом выбранном типе ВСЕГДА будут добавляться в выборку типы "any" в выбранной локации)*/
    void getListOfStates(String location, String type, MutableLiveData<ArrayList<String>> mStateIsList, MutableLiveData<ArrayList<String>> mNameList) {
        Log.e(TAG, "♦♦♦ getListOfStates: "+location);
        db.collection(TABLE_STATES)
                .whereEqualTo(STATE_LOCATION, location)
                .whereIn(STATE_TYPE, Arrays.asList(TYPE_ANY, type))
                .get().addOnCompleteListener(task -> {
            ArrayList<String> list1 = new ArrayList<>();
            ArrayList<String> list2 = new ArrayList<>();
            int count = 0;
            for (DocumentSnapshot document : task.getResult()) {
                Log.e(TAG, "getListOfStates: "+count);
                count++;
                String name = document.get(STATE_NAME).toString();
                String state_id = document.get(STATE_ID).toString();
                list1.add(state_id);
                list2.add(name);
                //todo если буду делать локализацию, то здесь надо будет вставлять что-то типа if(lang.isEng)name = "name_eng". В БД будет дополнительное поле "name_eng", оно будет выбираться вместо "name". И всё, весь остальной код уже будет работать. Это конечно касается только имени статуса, для других сделать аналогично
            }
            mStateIsList.setValue(list1);
            mNameList.setValue(list2);
        });
    }

}
