package com.example.githubuserapp2;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FollowerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowerFragment extends Fragment {

    RecyclerView rvFollower;
    private ArrayList<User> listUser = new ArrayList<>();
    private static final String TAG = FollowerFragment.class.getSimpleName();
    private ProgressBar progressBarFollower;

    public FollowerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //Mengambil data dari search user
        DetailActivity detailUserActivity = (DetailActivity) getActivity();
        User user = detailUserActivity.getIntent().getParcelableExtra(DetailActivity.EXTRA_USER);

        rvFollower = view.findViewById(R.id.rv_follower);
        progressBarFollower = view.findViewById(R.id.progress_bar_follower);
        rvFollower.setLayoutManager(new LinearLayoutManager(view.getContext()));

        getFollowerUser(user.getUsername());
    }


    private void getFollowerUser(final String username) {
        progressBarFollower.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.github.com/users/" + username + "/followers";

        client.addHeader("Authorization", "token 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");
        client.addHeader("User-Agent", "request");
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("login");
                        String avatarUrl = jsonObject.getString("avatar_url");

                        User user = new User();
                        user.setUsername(username);
                        user.setPhoto(avatarUrl);

                        listUser.add(user);
                    }
                    rvFollower.setAdapter(new ListUserAdapter(getContext(), listUser));
                    progressBarFollower.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBarFollower.setVisibility(View.INVISIBLE);
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
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

}