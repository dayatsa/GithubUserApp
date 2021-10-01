package com.example.githubuserapp2;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "extra_user_data";
    String name, user_company, repo, location;
    TextView tvName, tvUsername, tvLocation, tvRepo, tvCompany;
    ImageView ivAvatar;

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2
    };

    private static final String TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("Detail User");

        User user = getIntent().getParcelableExtra(EXTRA_USER);

        ivAvatar = findViewById(R.id.img_detail_avatar);
        tvUsername = findViewById(R.id.tv_detail_username);
        tvName = findViewById(R.id.tv_detail_name);
        tvLocation = findViewById(R.id.tv_detail_location);
        tvRepo = findViewById(R.id.tv_detail_repository);
        tvCompany = findViewById(R.id.tv_detail_company);

        final ProgressDialog progress = new ProgressDialog(DetailActivity.this);
        progress.setMessage(getString(R.string.progress));
        progress.show();

        if (user != null) {
            Glide.with(DetailActivity.this)
                    .load(user.getPhoto())
                    .into(ivAvatar);

            getDetailUser(user.getUsername(), progress);
            tabLayout();
        }
    }

    private void getDetailUser(final String username, ProgressDialog progress) {
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.github.com/users/" + username;

        client.addHeader("Authorization", "token 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");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    name = jsonObject.getString("name");
                    user_company = jsonObject.getString("company");
                    location = jsonObject.getString("location");
                    repo = jsonObject.getString("public_repos");

                    if (user_company == "null"){
                        user_company = " ";
                    }
                    if (location == "null"){
                        location = " ";
                    }
                    if (name == "null"){
                        name = " ";
                    }

                    tvUsername.setText(username);
                    tvName.setText(name);
                    tvLocation.setText(getString(R.string.txt_location) + location);
                    tvRepo.setText(getString(R.string.txt_repository) + repo);
                    tvCompany.setText(getString(R.string.txt_company) + user_company);
                    progress.dismiss();

                } catch (Exception e) {
                    Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progress.dismiss();
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage =  statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(DetailActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void tabLayout(){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        ViewPager2 viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(getResources().getString(TAB_TITLES[position]))
        ).attach();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
    }
}