package com.atomtex.repairstracker.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.atomtex.repairstracker.entities.DUnit;
import com.atomtex.repairstracker.adapter.EventsAdapter;
import com.atomtex.repairstracker.MainViewModel;
import com.atomtex.repairstracker.R;
import com.atomtex.repairstracker.utils.Utils;

public class InfoFragment extends Fragment {

    private TextView deviceName;
    private TextView serial;
    private TextView isComplete;
    private TextView daysPassed;
    private TextView trackId;

    public InfoFragment() {
    }

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        MainViewModel mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        deviceName = view.findViewById(R.id.device_name);
        serial = view.findViewById(R.id.serial_number);
        isComplete = view.findViewById(R.id.is_complete);
        isComplete.setVisibility(View.GONE);
        daysPassed = view.findViewById(R.id.daysPassedValue);
        trackId = view.findViewById(R.id.trackIdText);

        RecyclerView events = view.findViewById(R.id.recycler_view_events);
        EventsAdapter statesAdapter = new EventsAdapter();
        events.setLayoutManager(new LinearLayoutManager(getActivity()));
        events.setAdapter(statesAdapter);

        mViewModel.getSelectedUnit().observe(getViewLifecycleOwner(), this::insertDataToFields);
        mViewModel.getEventsForSelectedUnit().observe(getViewLifecycleOwner(), statesAdapter::setEvents);

        return view;
    }

    private void insertDataToFields(DUnit unit) {
        deviceName.setText(unit.getName());
        serial.setText(Utils.getRightValue(unit.getSerial()));
        isComplete.setVisibility(unit.isComplete()?View.VISIBLE:View.GONE);
        daysPassed.setText(String.valueOf(unit.daysPassed()));
        trackId.setText(unit.getTrackId());
    }
}