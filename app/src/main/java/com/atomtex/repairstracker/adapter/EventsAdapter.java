package com.atomtex.repairstracker.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import static com.atomtex.repairstracker.utils.Utils.getRightDate;
import static com.atomtex.repairstracker.utils.Utils.getRightTime;

import com.atomtex.repairstracker.R;
import com.atomtex.repairstracker.entities.DEvent;

/**Адаптер для списка всех статусов для выбранного конкретного устройства. Показывает дату, время и сам статус*/
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.StatesViewHolder> {

    private ArrayList<DEvent> events = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setEvents(ArrayList<DEvent> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    /**
     * Присваиваем xml лэйаут к итему RecyclerView
     */
    @NonNull
    @Override
    public StatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_state, parent, false);
        return new StatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatesViewHolder holder, int position) {
        DEvent event = events.get(position);

        holder.tState.setText(event.getState());
        holder.tLocation.setText(event.getLocation());
        long time = event.getDate().getTime();
        holder.tDate.setText(String.format("%s\n%s", getRightDate(time), getRightTime(time)));
    }

    /**
     * Просто возвращает кол-во элементов в массиве
     */
    @Override
    public int getItemCount() {
        return events.size();
    }

    static class StatesViewHolder extends RecyclerView.ViewHolder {
        private final TextView tDate;
        private final TextView tState;
        private final TextView tLocation;

        public StatesViewHolder(@NonNull View itemView) {
            super(itemView);
            tDate = itemView.findViewById(R.id.date);
            tState = itemView.findViewById(R.id.state);
            tLocation = itemView.findViewById(R.id.location2);
        }
    }
}
