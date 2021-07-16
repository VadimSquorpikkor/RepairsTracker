package com.atomtex.repairstracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    View view;
    MainViewModel mViewModel;
    TextView deviceName;
    TextView serial;
    RecyclerView events;

    public InfoFragment() {
    }

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        deviceName = view.findViewById(R.id.device_name);
        serial = view.findViewById(R.id.serial_number);
        events = view.findViewById(R.id.recycler_view_events);

        final MutableLiveData<DUnit> selectedUnits = mViewModel.getSelectedUnit();
        selectedUnits.observe(getViewLifecycleOwner(), this::insertDataToFields);

        final MutableLiveData<ArrayList<DEvent>> unitEvents = mViewModel.getEventsForSelectedUnit();
        unitEvents.observe(getViewLifecycleOwner(), this::updateAdapter);

        return view;
    }

    void updateAdapter(ArrayList<DEvent> list) {
        EventsAdapter statesAdapter = new EventsAdapter(list, mViewModel);
        events.setLayoutManager(new LinearLayoutManager(getActivity()));
        events.setAdapter(statesAdapter);
    }

    private void insertDataToFields(DUnit unit) {
//        deviceName.setText(getStringById(unit.getName()));
        deviceName.setText(unit.getName());
        serial.setText(Utils.getRightValue(unit.getSerial()));
    }
}