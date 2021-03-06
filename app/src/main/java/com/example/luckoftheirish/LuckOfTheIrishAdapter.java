package com.example.luckoftheirish;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LuckOfTheIrishAdapter extends RecyclerView.Adapter<LuckOfTheIrishAdapter.LuckOfTheIrishViewHolder>{

    private ArrayList<IrishPub> dataset;
    private ListListener listListener;

    public LuckOfTheIrishAdapter(ArrayList<IrishPub> dataset, ListListener listener){
        this.dataset = dataset;
        this.listListener = listener;
    }

    public class LuckOfTheIrishViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public ImageView imageView;

        public LuckOfTheIrishViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.pub_name);
            imageView = itemView.findViewById(R.id.pub_photo);

            imageView.setOnClickListener((view) ->{

                IrishPub irishPub = dataset.get(LuckOfTheIrishViewHolder.super.getAdapterPosition());
                listListener.onItemClicked(irishPub);

            });
        }
    }

    @NonNull
    @Override
    public LuckOfTheIrishAdapter.LuckOfTheIrishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        return new LuckOfTheIrishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LuckOfTheIrishAdapter.LuckOfTheIrishViewHolder holder, int position) {
        IrishPub irishPub = dataset.get(position);
        holder.name.setText(irishPub.getName());
        Picasso.get()
                .load(irishPub.getPhotoURL())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
