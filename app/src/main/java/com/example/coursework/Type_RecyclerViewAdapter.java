package com.example.coursework;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Type_RecyclerViewAdapter extends RecyclerView.Adapter<Type_RecyclerViewAdapter.MyViewHolder>{

    Context context;
    ArrayList<String> types, colours;

    public Type_RecyclerViewAdapter(Context context, ArrayList<String> types, ArrayList<String> colours) {
        this.context = context;
        this.types = types;
        this.colours = colours;
    }

    @NonNull
    @Override
    public Type_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.type_recycler_item, parent, false);
        return new Type_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Type_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.text.setText(types.get(position));
        holder.container.setCardBackgroundColor(Color.parseColor(colours.get(position)));
    }

    @Override
    public int getItemCount() {
        return types.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        CardView container;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            text = itemView.findViewById(R.id.timeTextView);
        }
    }
}
