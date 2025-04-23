package com.example.autocheckouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    List<Modal> list;

    public HistoryAdapter(List<Modal> list){
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_items,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Modal items = list.get(position);

        holder.name.setText(items.getName());
        holder.price.setText(items.getPrice());
        holder.timeStamp.setText(items.getTimeStamp());
        holder.img.setImageResource(items.getImg());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name, price,timeStamp;
        int count;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            timeStamp = itemView.findViewById(R.id.timestamp);
            price = itemView.findViewById(R.id.price);

        }
    }
}