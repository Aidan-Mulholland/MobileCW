package com.example.coursework;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Alarm_RecyclerViewAdapter extends RecyclerView.Adapter<Alarm_RecyclerViewAdapter.MyViewHolder>{
    private final AlarmRecyclerInterface recyclerInterface;

    Context context;
    ArrayList<AlarmModel> alarms;

    public Alarm_RecyclerViewAdapter(Context context, ArrayList<AlarmModel> alarms, AlarmRecyclerInterface recyclerInterface) {
        this.context = context;
        this.alarms = alarms;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public Alarm_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sleep_recycler_item, parent, false);
        return new Alarm_RecyclerViewAdapter.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Alarm_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.text.setText(alarms.get(position).getTimeText());
        holder.toggle.setToggle(alarms.get(position).getActive());
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        ToggleButtonView toggle;
        CardView container;
        public MyViewHolder(@NonNull View itemView, AlarmRecyclerInterface recyclerInterface) {
            super(itemView);
            text = itemView.findViewById(R.id.timeTextView);
            toggle = itemView.findViewById(R.id.toggleButtonView);
            toggle.setOnClickListener(view -> {
                if (recyclerInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerInterface.onItemClick(position);
                    }
                }
            });
            container = itemView.findViewById(R.id.container);
            container.setOnLongClickListener(view -> {
                boolean result = false;
                if (recyclerInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        result = recyclerInterface.onLongClick(position);
                    }
                }
                return result;
            });
        }

    }
}
