package com.atomtex.repairstracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import static com.atomtex.repairstracker.Utils.EMPTY_VALUE;
import static com.atomtex.repairstracker.Utils.daysPassed;
import static com.atomtex.repairstracker.Utils.getNameById;
import static com.atomtex.repairstracker.Utils.getRightDateAndTime;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.DUnitViewHolder>{

    private final ArrayList<DUnit> units;

    MainViewModel mViewModel;

    /**Конструктор, в котором передаем ArrayList для RecyclerView */
    public UnitAdapter(ArrayList<DUnit> units, MainViewModel model) {
        this.units = units;
        this.mViewModel = model;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**Присваиваем xml лэйаут к итему RecyclerView */
    @NonNull
    @Override
    public DUnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dunit, parent, false);
        return new DUnitViewHolder(view);
    }

    /**Принимает объект ViewHolder (holder) и порядковый номер элемента массива (position)
    * т.е. у 1-ого элемента View будет порядковый номер 0, он возмёт элемент с этим индексом (заметку)
    * и у ViewHolder-а установить все значения (присвоить значения к TextView) */
    @Override
    public void onBindViewHolder(@NonNull DUnitViewHolder holder, int position) {
        DUnit unit = units.get(position);

        String state = getNameById(unit.getState(), mViewModel.getAllStatesNameList().getValue(), Objects.requireNonNull(mViewModel.getAllStatesIdList().getValue()));
        String name = getNameById(unit.getName(), mViewModel.getDeviceNameList().getValue(), Objects.requireNonNull(mViewModel.getDeviceIdList().getValue()));

        holder.tState.setText(state);
        holder.tName.setText(name);
        holder.tSerial.setText(String.format("№ %s", Utils.getRightValue(unit.getSerial())));
        if (unit.getInnerSerial()==null||unit.getInnerSerial().equals(""))holder.tInnerSerial.setText("");
        else holder.tInnerSerial.setText(String.format("(вн. %s)", unit.getInnerSerial()));

        if (unit.getDate()!=null){
//            holder.tDate.setVisibility(View.VISIBLE);
            holder.tDate.setText(getRightDateAndTime(unit.getDate().getTime()));
//            int days = daysPassed(unit.getDate());
            holder.tDatePassed.setText(String.format("%s д.", daysPassed(unit.getDate())));
        } else {
//            holder.tDate.setVisibility(View.GONE);
            holder.tDate.setText(EMPTY_VALUE);
            holder.tDatePassed.setText("");
        }
    }

    /**Просто возвращает кол-во элементов в массиве*/
    @Override
    public int getItemCount() {
        return units.size();
    }

    class DUnitViewHolder extends RecyclerView.ViewHolder {
        private final TextView tName;
        private final TextView tInnerSerial;
        private final TextView tSerial;
        private final TextView tState;
        private final TextView tDate;
        private final TextView tDatePassed;

        public DUnitViewHolder(@NonNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.textName);
            tInnerSerial = itemView.findViewById(R.id.textInnerSerial);
            tSerial = itemView.findViewById(R.id.textSerial);
            tState = itemView.findViewById(R.id.textState);
            tDate = itemView.findViewById(R.id.textDate);
            tDatePassed = itemView.findViewById(R.id.textDatePassed);

            //для работы OnNoteClickListener
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}