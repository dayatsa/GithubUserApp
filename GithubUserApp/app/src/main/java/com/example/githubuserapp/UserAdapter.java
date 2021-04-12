package com.example.githubuserapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.githubuserapp.databinding.ItemUserBinding;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<User> user = new ArrayList<>();

    public void setUser(ArrayList<User> user) {
        this.user = user;
    }

    public UserAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int position) {
        return user.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);

        User user = (User)getItem(position);
        viewHolder.bind(user);
        return itemView;
    }

    private class ViewHolder {
        private final ItemUserBinding binding;

        ViewHolder(View view) {
            binding = ItemUserBinding.bind(view);
        }

        void bind(User user){
            binding.txtName.setText(user.getName());
            binding.txtCompany.setText(user.getUserCompany());
            binding.txtLocation.setText(user.getUserLocation());
            binding.imgPhoto.setImageResource(user.getPhoto());
        }
    }
}

