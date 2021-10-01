package com.example.githubuserapps3

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.githubuserapps3.adapter.FavoriteAdapter
import com.example.githubuserapps3.adapter.SectionsPagerAdapter
import com.example.githubuserapps3.data.Favorite
import com.example.githubuserapps3.data.User
import com.example.githubuserapps3.db.DatabaseContract
import com.example.githubuserapps3.db.FavoriteHelper
import com.example.githubuserapps3.helper.MappingHelper
import com.google.android.material.tabs.TabLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject


lateinit var dcontext: Context
class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private val TAG = DetailActivity::class.java.simpleName
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var ivAvatar: CircleImageView
    private lateinit var tvUsername: TextView
    private lateinit var tvName: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvRepo: TextView
    private lateinit var tvCompany: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var buttonFavorite: AppCompatImageButton

    private var position: Int? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private var isFavorite: Boolean = false

    private lateinit var username: String
    private lateinit var avatar: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        dcontext = this

        ivAvatar = findViewById(R.id.detail_avatar);
        tvUsername = findViewById(R.id.detail_username);
        tvName = findViewById(R.id.detail_name);
        tvLocation = findViewById(R.id.detail_location);
        tvRepo = findViewById(R.id.detail_repository);
        tvCompany = findViewById(R.id.detail_company);
        viewPager = findViewById(R.id.view_pager)
        tabs = findViewById(R.id.tabs)
        buttonFavorite = findViewById(R.id.btn_favorite)

        username = intent.getStringExtra(EXTRA_NAME).toString()
        isFavorite = checkFavorite()

        if (isFavorite) {
            position = intent.getIntExtra(EXTRA_POSITION, -1)
            val checked: Int = R.drawable.ic_baseline_favorite_red_24
            buttonFavorite.setImageResource(checked)
        } else {
            val unChecked: Int = R.drawable.ic_baseline_favorite_border_24
            buttonFavorite.setImageResource(unChecked)
        }


        buttonFavorite.setOnClickListener(this)
        supportActionBar?.title = username
        getDetailUser(username)
        viewPagerConfig()
    }

    private fun checkFavorite(): Boolean {
        var isTrue = false
        val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        val cursor = favoriteHelper.queryByName(username)
        val favorite =  MappingHelper.mapCursorToArrayList(cursor)
        if (favorite.size > 0) {
            isTrue = true
        }
        favoriteHelper.close()
        return isTrue
    }


    override fun onClick(view: View) {
        val checked: Int = R.drawable.ic_baseline_favorite_red_24
        val unChecked: Int = R.drawable.ic_baseline_favorite_border_24
        if (view.id == R.id.btn_favorite) {
            favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()

            if (isFavorite) {
                buttonFavorite.setImageResource(unChecked)
                isFavorite = false

                val result = favoriteHelper.deleteById(position.toString()).toLong()
                if (result > 0) {
                    Toast.makeText(this@DetailActivity, "Berhasil menghapus dari favorit", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal menghapus", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                buttonFavorite.setImageResource(checked)
                isFavorite = true

                val values = ContentValues()
                values.put(DatabaseContract.UserColumns.USERNAME, username)
                values.put(DatabaseContract.UserColumns.AVATAR, avatar)

                val result = favoriteHelper.insert(values)


                if (result > 0) {
                    position = result.toInt()
                    Toast.makeText(this@DetailActivity, "Berhasil ditambahkan ke favorit", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@DetailActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
                }
            }

            favoriteHelper.close()
        }
    }

    private fun viewPagerConfig() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)

        supportActionBar?.elevation = 0f
    }


    private fun getDetailUser(username: String) {
        val client = AsyncHttpClient()
        val clientSecret = "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"
        val url =
            "https://api.github.com/users/$username"

        client.addHeader("Authorization", "token $clientSecret")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d(DetailActivity.TAG, result)
                try {
                    val jsonObject = JSONObject(result)
                    avatar = jsonObject.getString("avatar_url")
                    var name = jsonObject.getString("name")
                    var userCompany = jsonObject.getString("company")
                    var location = jsonObject.getString("location")
                    val repo = jsonObject.getString("public_repos")

                    if (userCompany == "null") {
                        userCompany = " ";
                    }
                    if (location == "null") {
                        location = " ";
                    }
                    if (name == "null") {
                        name = " ";
                    }
                    Log.d("data", avatar)

                    Glide.with(dcontext).load(avatar).into(ivAvatar)
                    tvUsername.setText(username)
                    tvName.setText(name)
                    tvCompany.text = getString(R.string.company) + " " + userCompany
                    tvLocation.text = getString(R.string.location) + " " + location
                    tvRepo.text = getString(R.string.repository) + " " + repo

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}