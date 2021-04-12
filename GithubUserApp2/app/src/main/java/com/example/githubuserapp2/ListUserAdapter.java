package com.example.githubuserapp2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListUserAdapter extends RecyclerView.Adapter<ListUserAdapter.ListViewHolder> {
    private ArrayList<User> listUser;
    Context context;

    public ListUserAdapter(Context context, ArrayList<User> list) {
        this.context = context;
        this.listUser = list;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        User user = listUser.get(position);
        Glide.with(holder.itemView.getContext())
                .load(user.getPhoto())
                .into(holder.imgPhoto);
        holder.tvUsername.setText(user.getUsername());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ListClick", user.getUsername());

                Intent moveWithObjectActivity = new Intent(context, DetailActivity.class);
                moveWithObjectActivity.putExtra(DetailActivity.EXTRA_USER, user);
                context.startActivity(moveWithObjectActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvUsername;
        ListViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvUsername = itemView.findViewById(R.id.tv_item_username);
        }
    }
}
