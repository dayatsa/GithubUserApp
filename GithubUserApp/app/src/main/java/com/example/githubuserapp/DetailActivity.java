package com.example.githubuserapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.githubuserapp.databinding.ActivityDetailBinding;
import com.example.githubuserapp.databinding.ItemUserBinding;

public class DetailActivity extends AppCompatActivity {
    String name, username, user_company, repo, following, followers, location;
    int path;
    public static final String EXTRA_USER = "extra_user_data";

    private ActivityDetailBinding bindingDetail;

    TextView tViewName, tViewUserName, tViewUserCompany, tViewRepo,
            textViewFollowing, tViewFollowers, tViewLocation;

    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bindingDetail = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(bindingDetail.getRoot());

        User user = getIntent().getParcelableExtra(EXTRA_USER);

        if (user != null) {
            name = user.getName();
            bindingDetail.tvDetailName.setText(name);
            username = user.getUsername();
            bindingDetail.tvDetailUsername.setText(getString(R.string.txt_username) + username);
            user_company = user.getUserCompany();
            bindingDetail.tvDetailCompany.setText(getString(R.string.txt_company) + user_company);
            repo = user.getRepo();
            bindingDetail.tvDetailRepository.setText(getString(R.string.txt_repository) + repo);
            following = user.getFollowing();
            bindingDetail.tvDetailFollowing.setText(getString(R.string.txt_following) + following);
            followers = user.getFollowers();
            bindingDetail.tvDetailFollowers.setText(getString(R.string.txt_followers) + followers);
            location = user.getUserLocation();
            bindingDetail.tvDetailLocation.setText(getString(R.string.txt_location) + location);
            path = user.getPhoto();
            bindingDetail.imgDetailAvatar.setImageResource(path);
        }
    }
}