package com.atomtex.repairstracker;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

class Dictionary {

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

    public static String getStringById(String id) {
        int res = myMap.containsKey(id)?myMap.get(id):R.string.empty_value;
        return App.getContext().getResources().getString(res);
    }
}
