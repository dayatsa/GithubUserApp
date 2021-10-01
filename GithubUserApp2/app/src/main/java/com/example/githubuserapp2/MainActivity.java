package com.example.githubuserapp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvUser;
    private ArrayList<User> listUser = new ArrayList<>();
    private ProgressBar progressBar;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Github User's Search");
        }

        rvUser = findViewById(R.id.rv_user);
        progressBar = findViewById(R.id.progress_bar);

        rvUser.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        showProgress(false);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchManager != null) {
            SearchView searchView = (SearchView) findViewById(R.id.sv_search);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.find_username));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    showProgress(true);
                    if (query != null){
                        getListUser(query);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Please Input Username!", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
    }


    private void showProgress(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void getListUser(final String username) {
        AsyncHttpClient client = new AsyncHttpClient();
        listUser.clear();

        String clientId = "dayatsa";
        String clientSecret = "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";
        String url = "https://api.github.com/search/users?q=" + username + "&client_id=" + clientId + "&client_secret=" + clientSecret;

        client.addHeader("Authorization", "token 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");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Jika koneksi berhasil

                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONArray jsonArray = new JSONArray(new JSONObject(result).getString("items"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("login");
                        String avatarUrl = jsonObject.getString("avatar_url");

                        User user = new User();
                        user.setUsername(username);
                        user.setPhoto(avatarUrl);

                        listUser.add(user);
                    }
                    rvUser.setAdapter(new ListUserAdapter(MainActivity.this, listUser));
                    showProgress(false);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
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
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}