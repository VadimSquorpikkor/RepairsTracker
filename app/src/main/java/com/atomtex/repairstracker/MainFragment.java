package com.atomtex.repairstracker;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import static com.atomtex.repairstracker.ThemeUtils.THEME_DARK;
import static com.atomtex.repairstracker.ThemeUtils.THEME_LIGHT;
import static com.atomtex.repairstracker.ThemeUtils.getTheme;


public class MainFragment extends Fragment {

    View view;
    MainViewModel mainViewModel;
    SwitchCompat themeSwitch;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

//        final MutableLiveData<Boolean> switcherState = mainViewModel.getSwitcherState();
//        switcherState.observe(requireActivity(), aBoolean -> themeSwitch.setChecked(aBoolean));

        themeSwitch = view.findViewById(R.id.theme_switch);
        themeSwitch.setChecked(getTheme()==2);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> changeTheme(themeSwitch.isChecked()));

        return view;
    }

    /**По положению свича устанавливает нужную тему: светлую или темную*/
    //todo вместо свича сделать картинку (солнце/луна)
    void changeTheme(boolean newState) {
        if (newState)ThemeUtils.changeToTheme(requireActivity(), THEME_DARK);
        else ThemeUtils.changeToTheme(requireActivity(), THEME_LIGHT);
    }
}