package com.example.autocheckouts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Items> list;
    onItemClickListener listener;

    public interface onItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(onItemClickListener clickListener){
         listener = clickListener;
    }
    public MyAdapter(List<Items> list){
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        return new MyViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Items items = list.get(position);

        holder.name.setText(items.getName());
        holder.price.setText(items.getPrice());
        holder.img.setImageResource(items.getImg());

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(
                holder.itemView.getContext(),
                android.R.layout.simple_spinner_item,
                getQuantityList()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(adapter);

// Set default quantity (e.g. 1)
        holder.spinner.setSelection(items.getQuantity() - 1);

// Handle quantity selection changes
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                items.setQuantity((Integer) parent.getItemAtPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView name, price;
        ImageButton button;
        Spinner spinner;

        public MyViewHolder(@NonNull View itemView,onItemClickListener listener) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            button = itemView.findViewById(R.id.button);
            spinner = itemView.findViewById(R.id.quantity_spinner);


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
    private List<Integer> getQuantityList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        return list;
    }

}

