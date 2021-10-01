package com.example.githubuserapps3

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserapps3.adapter.FavoriteAdapter
import com.example.githubuserapps3.adapter.ListUserAdapter
import com.example.githubuserapps3.db.FavoriteHelper
import com.example.githubuserapps3.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import androidx.core.app.ActivityCompat.startActivityForResult




lateinit var fcontext: Context



class FavoriteActivity : AppCompatActivity() {

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            Toast.makeText(this@FavoriteActivity, "masukkkk", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private val TAG = FavoriteActivity::class.java.simpleName
        private const val MY_REQUEST = 1001
    }

    private lateinit var rvFavorite: RecyclerView
    private lateinit var progressBarFavorite: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = "Favorite Users"

        rvFavorite = findViewById(R.id.recycleViewFavorite)
        progressBarFavorite = findViewById(R.id.progressBarFavorite)
        progressBarFavorite.visibility = View.INVISIBLE

        recyclerViewConfig()
        loadFavoriteAsync()
    }

    override fun onResume() {
        super.onResume()
        recyclerViewConfig()
        loadFavoriteAsync()
    }

    private fun recyclerViewConfig() {
        rvFavorite.layoutManager = LinearLayoutManager(rvFavorite.context)
        rvFavorite.setHasFixedSize(true)
        rvFavorite.addItemDecoration(
            DividerItemDecoration(
                rvFavorite.context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun loadFavoriteAsync() {
        lifecycleScope.launch {
            progressBarFavorite.visibility = View.VISIBLE
            val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBarFavorite.visibility = View.INVISIBLE
            val favorite = deferredNotes.await()
            if (favorite.size > 0) {
                val adapter = FavoriteAdapter(favorite)
                rvFavorite.adapter = adapter
            } else {
                Log.d(FavoriteActivity.TAG, "tidak ada")
            }
            favoriteHelper.close()
        }
    }
}