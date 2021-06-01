package com.atomtex.repairstracker;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import static com.atomtex.repairstracker.ThemeUtils.THEME_DARK;
import static com.atomtex.repairstracker.ThemeUtils.THEME_LIGHT;
import static com.atomtex.repairstracker.ThemeUtils.getTheme;

public class MainFragment extends Fragment {

    View view;
    MainViewModel mViewModel;
    SwitchCompat themeSwitch;
    RecyclerView foundUnitRecycler;
    ImageView logoImage;

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
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        logoImage = view.findViewById(R.id.logo_image);

        final MutableLiveData<ArrayList<DUnit>> units = mViewModel.getUnitListToObserve();
        units.observe(getViewLifecycleOwner(), this::updateFoundRecycler);

        themeSwitch = view.findViewById(R.id.theme_switch);
        themeSwitch.setChecked(getTheme()==2);
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> changeTheme(themeSwitch.isChecked()));

        foundUnitRecycler = view.findViewById(R.id.found_unit_recycler);

        view.findViewById(R.id.add_device).setOnClickListener(v -> {
            mViewModel.openAddDeviceDialog(requireFragmentManager());
        });

        return view;
    }

    /**По положению свича устанавливает нужную тему: светлую или темную*/
    //todo вместо свича сделать картинку (солнце/луна)
    void changeTheme(boolean newState) {
        if (newState)ThemeUtils.changeToTheme(requireActivity(), THEME_DARK);
        else ThemeUtils.changeToTheme(requireActivity(), THEME_LIGHT);
    }

    private void updateFoundRecycler(ArrayList<DUnit> list) {
        if (list == null) return;
        if (list.size() == 0) {
            logoImage.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.nothing_found), Toast.LENGTH_SHORT).show();
        } else {
            logoImage.setVisibility(View.GONE);
        }
        UnitAdapter unitAdapter = new UnitAdapter(list, mViewModel);
        unitAdapter.setOnItemClickListener(this::showEvents);
        foundUnitRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundUnitRecycler.setAdapter(unitAdapter);
    }

    private void showEvents(int position) {
        mViewModel.showEvents(position, requireActivity().getSupportFragmentManager());
    }
}