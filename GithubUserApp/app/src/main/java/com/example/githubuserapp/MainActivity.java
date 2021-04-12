package com.example.githubuserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private UserAdapter adapter;
    private String[] dataName;
    private String[] dataUsername;
    private String[] dataRepo;
    private String[] dataCompany;
    private String[] dataLocation;
    private String[] dataFollowing;
    private String[] dataFollowers;
    private TypedArray dataPhoto;
    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.lv_list);
        adapter = new UserAdapter(this);
        listView.setAdapter(adapter);
        prepare();
        addItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = new User();
                user.setPhoto(dataPhoto.getResourceId(position, -1));
                user.setName(dataName[position]);
                user.setUserLocation(dataLocation[position]);
                user.setRepo(dataRepo[position]);
                user.setUsername(dataUsername[position]);
                user.setFollowing(dataFollowing[position]);
                user.setUserCompany(dataCompany[position]);
                user.setFollowers(dataFollowers[position]);

                Intent moveWithObjectActivity = new Intent(MainActivity.this, DetailActivity.class);
                moveWithObjectActivity.putExtra(DetailActivity.EXTRA_USER, user);
                startActivity(moveWithObjectActivity);
            }
        });
    }
    private void prepare() {
        dataName = getResources().getStringArray(R.array.name);
        dataUsername = getResources().getStringArray(R.array.username);
        dataRepo = getResources().getStringArray(R.array.repository);
        dataCompany = getResources().getStringArray(R.array.company);
        dataLocation = getResources().getStringArray(R.array.location);
        dataFollowing = getResources().getStringArray(R.array.following);
        dataFollowers = getResources().getStringArray(R.array.followers);
        dataPhoto = getResources().obtainTypedArray(R.array.avatar);
    }

    private void addItem() {
        users = new ArrayList<>();
        for (int i = 0; i < dataName.length; i++) {
            User user = new User();
            user.setPhoto(dataPhoto.getResourceId(i, -1));
            user.setName(dataName[i]);
            user.setUsername(dataUsername[i]);
            user.setRepo(dataRepo[i]);
            user.setUserCompany(dataCompany[i]);
            user.setUserLocation(dataLocation[i]);
            user.setFollowing(dataFollowing[i]);
            user.setFollowers(dataFollowers[i]);
            users.add(user);
        }
        adapter.setUser(users);
    }
}