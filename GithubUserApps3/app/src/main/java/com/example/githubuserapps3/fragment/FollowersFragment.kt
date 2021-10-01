package com.example.githubuserapps3.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapps3.R
import com.example.githubuserapps3.data.User
import com.example.githubuserapps3.adapter.ListUserAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray


class FollowersFragment : Fragment() {
    companion object {
        private val TAG = FollowersFragment::class.java.simpleName
        const val EXTRA_NAME = "extra_name"
    }

    private lateinit var rvUserFollowers: RecyclerView
    private var listUser = ArrayList<User>()
    private lateinit var progressBarFollower: ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)

//        val username = intent.getStringExtra(DetailActivity.EXTRA_NAME)

        rvUserFollowers = view.findViewById(R.id.recycleViewFollowers)
        progressBarFollower = view.findViewById(R.id.progressBarFollowers)

        var username = activity?.intent?.getStringExtra(EXTRA_NAME)
        recyclerViewConfig()
        getFollowersUser(username.toString())
    }


    private fun getFollowersUser(username: String?) {
        progressBarFollower.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username/followers"
        val clientSecret = "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"

        client.addHeader("Authorization", "token $clientSecret")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                progressBarFollower.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val username = jsonObject.getString("login")
                        val avatar = jsonObject.getString("avatar_url")

                        val user = User()
                        user.username = username
                        user.avatar = avatar

                        listUser.add(user)
                    }

                    val adapter = ListUserAdapter(listUser)
                    rvUserFollowers.adapter = adapter

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                progressBarFollower.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recyclerViewConfig() {
        rvUserFollowers.layoutManager = LinearLayoutManager(rvUserFollowers.context)
        rvUserFollowers.setHasFixedSize(true)
        rvUserFollowers.addItemDecoration(
            DividerItemDecoration(
                rvUserFollowers.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}