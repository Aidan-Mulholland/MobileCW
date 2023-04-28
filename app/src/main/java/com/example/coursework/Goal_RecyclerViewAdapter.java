package com.example.coursework;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Goal_RecyclerViewAdapter extends RecyclerView.Adapter<Goal_RecyclerViewAdapter.MyViewHolder>{
    private final GoalRecyclerInterface recyclerInterface;

    Context context;
    static ArrayList<GoalModel> goals;
    ArrayList<String> colours;

    public Goal_RecyclerViewAdapter(Context context, ArrayList<GoalModel> goals, ArrayList<String> colours, GoalRecyclerInterface recyclerInterface) {
        this.context = context;
        this.goals = goals;
        this.colours = colours;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public Goal_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.goal_recycler_item, parent, false);
        return new Goal_RecyclerViewAdapter.MyViewHolder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Goal_RecyclerViewAdapter.MyViewHolder holder, int position) {
        // Set values for the layout
        holder.title.setText(goals.get(position).getName());
        holder.description.setText(goals.get(position).getDescription());
        holder.progressBar.setProgress(goals.get(position).getProgress(), true);
        holder.container.setCardBackgroundColor(Color.parseColor(colours.get(position)));
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        CardView container;
        ProgressBar progressBar;
        Button editBtn, deleteBtn;
        public MyViewHolder(@NonNull View itemView, GoalRecyclerInterface recyclerInterface) {
            super(itemView);
            title = itemView.findViewById(R.id.GoalTitle);
            description = itemView.findViewById(R.id.GoalDescription);
            progressBar = itemView.findViewById(R.id.progressBar);
            container = itemView.findViewById(R.id.container);
            container.setOnClickListener(view -> {
                if (recyclerInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        recyclerInterface.onItemClick(position);
                    }
                }
            });
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

