package com.atomtex.repairstracker.utils;

import com.atomtex.repairstracker.MainViewModel;
import com.atomtex.repairstracker.R;
import com.atomtex.repairstracker.app.App;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Dictionary {

    private static final Map<String, Integer> myMap;
    static {
        Map<String, Integer> aMap = new TreeMap<>();
        aMap.put("adjustment", R.string.adjustment);
        aMap.put("assembly", R.string.assembly);
        aMap.put("graduation", R.string.graduation);
        aMap.put("repair_area", R.string.repair_area);
        aMap.put("soldering", R.string.soldering);
        aMap.put("adj_r_diagnostica", R.string.adj_r_diagnostica);
        aMap.put("adj_r_ispytania", R.string.adj_r_ispytania);
        aMap.put("adj_r_utochnenie", R.string.adj_r_utochnenie);
        aMap.put("adj_s_calibrovka", R.string.adj_s_calibrovka);
        aMap.put("adj_s_nastroika", R.string.adj_s_nastroika);
        aMap.put("adj_s_otgruzka", R.string.adj_s_otgruzka);
        aMap.put("ass_a_razborka", R.string.ass_a_razborka);
        aMap.put("ass_a_sborka", R.string.ass_a_sborka);
        aMap.put("ass_a_zamena", R.string.ass_a_zamena);
        aMap.put("gra_a_graduirovka", R.string.gra_a_graduirovka);
        aMap.put("gra_a_psi", R.string.gra_a_psi);
        aMap.put("rep_r_prinyat", R.string.rep_r_prinyat);
        aMap.put("rep_r_raschet", R.string.rep_r_raschet);
        aMap.put("rep_r_soglasovanie", R.string.rep_r_soglasovanie);
        aMap.put("sol_a_montag", R.string.sol_a_montag);
        aMap.put("AT2503", R.string.AT2503);
        aMap.put("AT6130", R.string.AT6130);
        aMap.put("AT6130C", R.string.AT6130C);
        aMap.put("BDKG-01", R.string.BDKG01);
        aMap.put("BDKG-04", R.string.BDKG04);
        myMap = Collections.unmodifiableMap(aMap);
    }

    public static int getResById_(String id) {
        int res;
        if (myMap.get(id)==null) res = R.string.empty_value;
        else res = myMap.get(id);
        return res;
    }

    /**Имя берет из сохраненных в устройство стрингов*/
    public static String getStringById(String id) {
        int res = myMap.containsKey(id)?myMap.get(id):R.string.empty_value;
        return App.getContext().getResources().getString(res);
    }

    public static final String EN = "en";
    public static final String RU = "ru";

    public static String getStringByIdIfExistsOrDownloadIfNot(String id, MainViewModel model) {
        if (myMap.containsKey(id)) {

        }

        int res = myMap.containsKey(id)?myMap.get(id):R.string.empty_value;
        return App.getContext().getResources().getString(res);
    }



    //todo сделать кэширование имен при загрузке из БД (в hashSet), а при каждом запросе перед попыткой загрузить имя (getNameById)
    // проверять, есть ли такое имя в кэше, если есть, то брать из кэша, если нет — загружать. Так, если будут одинаковые имена,
    // не придется загружать по несколько раз одно и то же. Разумеется при перезагрузке приложения кэш обнуляется (а иначе как узнать,
    // может данные в БД уже другие)


    /**У использования стрингов из библиотеки есть плюсы и минусы:
     * плюсы:
     * — это простота реализации,
     * — легко добавлять/изменять,
     * — уже работает многоязычность, просто добавляется новый strings.xml под каждый новый язык
     * — при переключении языка в настройках телефона сразу же автоматом меняется язык интерфейса
     * минусы:
     * — если нужно изменить например имя устройства (АТ6130 -> МКС-АТ6130) или добавить новое (а добавляться будт часто),
     * то, чтобы пользователь увидел устройство под этим новым именем, ему нужно скачать новую версию приложения
     *
     * Загружать данные из БД:
     * минусы:
     * — больше кода
     * — сложнее добавлять новые данные, с другой стороны для этого уже сделана веб форма, с помощью неё всё гораздо проще
     * — многоязычность реализуется через свои методы, а не автоматом
     * — при переключении языка в настройках телефона язык в UI не меняется, нужно перезапускать приложение
     * — главный минус: каждый стринг — это ещё один запрос в БД, итого даже просто загрузка одного юнита это 4 запроса в БД вместо 1,
     * при увеличении количества загружаемых юнитов кол-во запросов растет геометрически
     * плюсы:
     * — самый главный плюс такого подхода, это: если вдруг изменилось имя устройства/локации/статуса
     * или добавилось новое устройство/статус/локация или добавился новый язык в БД, то эти изменения
     * уже будут видны автоматом без установки новой версии (в приложении с лисенерами AdjustmentDB эти изменения
     * были видны на UI в работающем приложении сразу при изменении в БД!)
     * * */


}
