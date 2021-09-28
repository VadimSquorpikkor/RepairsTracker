package com.atomtex.repairstracker.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import static com.atomtex.repairstracker.utils.Utils.EMPTY_VALUE;
import static com.atomtex.repairstracker.utils.Utils.daysPassed;
import static com.atomtex.repairstracker.utils.Utils.getRightDateAndTime;

import com.atomtex.repairstracker.R;
import com.atomtex.repairstracker.app.App;
import com.atomtex.repairstracker.entities.DUnit;
import com.atomtex.repairstracker.utils.Utils;

public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.DUnitViewHolder>{

    private ArrayList<DUnit> units = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setUnits(ArrayList<DUnit> units) {
        this.units = units;
        notifyDataSetChanged();
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

        holder.tState.setText(unit.getState());
        holder.tName.setText(unit.getName());
        holder.tSerial.setText(String.format(App.getContext().getString(R.string.serial_number_prefix), Utils.getRightValue(unit.getSerial())));
        holder.tIsComplete.setVisibility(unit.isComplete()?View.VISIBLE:View.GONE);
        holder.tTrackId.setText("ID: "+unit.getTrackId());

        if (unit.getDate()!=null){
            holder.tDate.setText(getRightDateAndTime(unit.getDate().getTime()));
            holder.tDatePassed.setText(String.format(App.getContext().getString(R.string.days), unit.daysPassed()));
        } else {
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
        private final TextView tSerial;
        private final TextView tState;
        private final TextView tDate;
        private final TextView tDatePassed;
        private final TextView tIsComplete;
        private final TextView tTrackId;

        public DUnitViewHolder(@NonNull View itemView) {
            super(itemView);
            tName = itemView.findViewById(R.id.textName);
            tSerial = itemView.findViewById(R.id.textSerial);
            tState = itemView.findViewById(R.id.textState);
            tDate = itemView.findViewById(R.id.textDate);
            tDatePassed = itemView.findViewById(R.id.textDatePassed);
            tIsComplete = itemView.findViewById(R.id.isComplete);
            tTrackId = itemView.findViewById(R.id.textViewTrackId);

            //для работы OnNoteClickListener
            itemView.setOnClickListener(view -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(DUnitViewHolder.this.getAdapterPosition());
                }
            });

        }
    }
}