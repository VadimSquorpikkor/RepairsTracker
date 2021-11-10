package com.atomtex.repairstracker.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import static com.atomtex.repairstracker.utils.ThemeUtils.THEME_DARK;
import static com.atomtex.repairstracker.utils.ThemeUtils.THEME_LIGHT;
import static com.atomtex.repairstracker.utils.ThemeUtils.getTheme;

import com.atomtex.repairstracker.entities.DUnit;
import com.atomtex.repairstracker.MainViewModel;
import com.atomtex.repairstracker.R;
import com.atomtex.repairstracker.dialog.SearchUnitParamsDialog;
import com.atomtex.repairstracker.adapter.UnitAdapter;
import com.atomtex.repairstracker.utils.ThemeUtils;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private ImageView logoImage;
    private SwipeRefreshLayout refreshView;
    private UnitAdapter unitAdapter;

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
        ThemeUtils.onActivityCreateSetTheme(getActivity());
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        logoImage = view.findViewById(R.id.logo_image);

        mViewModel.getUnitListToObserve().observe(getViewLifecycleOwner(), this::updateFoundRecycler);

        RecyclerView foundUnitRecycler = view.findViewById(R.id.found_unit_recycler);
        unitAdapter = new UnitAdapter();
        unitAdapter.setOnItemClickListener(this::showEvents);
        foundUnitRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        foundUnitRecycler.setAdapter(unitAdapter);

        view.findViewById(R.id.change_theme).setOnClickListener(v -> changeTheme());
        view.findViewById(R.id.refresh).setOnClickListener(v -> refresh());
        view.findViewById(R.id.add_device).setOnClickListener(v -> openAddDeviceDialog());

        refreshView = view.findViewById(R.id.refreshView);

        //Чтобы пользоваться свайпом, добавляем
        //2-й параметр -- это направление
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //что-то делаем по свайпу вправо или влево
                removeItem(viewHolder.getAdapterPosition());
            }
        });

        //присваиваем хелпер к ресайклеру
        itemTouchHelper.attachToRecyclerView(foundUnitRecycler);

        // refresh RecyclerView
        refreshView.setOnRefreshListener(this::refresh);

        return view;
    }

    private void removeItem(int adapterPosition) {
        mViewModel.removeItemFromList(adapterPosition);
    }

    private void refresh() {
        mViewModel.refresh();
    }

    /**По тапу на иконку устанавливает нужную тему: светлую, если сейчас темная, и наоборот*/
    void changeTheme() {
        if (getTheme()==THEME_LIGHT)ThemeUtils.changeToTheme(requireActivity(), THEME_DARK);
        else ThemeUtils.changeToTheme(requireActivity(), THEME_LIGHT);
    }

    private void updateFoundRecycler(ArrayList<DUnit> list) {
        mViewModel.updateUnitsTrackIdNumbersList();
        if (list == null) return;
        if (list.size() == 0) {
            logoImage.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), getString(R.string.nothing_found), Toast.LENGTH_SHORT).show();
        } else {
            logoImage.setVisibility(View.GONE);
        }
        unitAdapter.setUnits(list);
        refreshView.setRefreshing(false);
    }

    private void showEvents(int position) {
        mViewModel.showEvents(position, requireActivity().getSupportFragmentManager());
    }

    public void openAddDeviceDialog() {
        SearchUnitParamsDialog dialog = new SearchUnitParamsDialog();
        dialog.show(getParentFragmentManager(), null);
    }
}