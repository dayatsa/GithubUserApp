package com.example.githubuserapps3.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapps3.DetailActivity
import com.example.githubuserapps3.MainActivity
import com.example.githubuserapps3.R
import com.example.githubuserapps3.data.Favorite
import com.example.githubuserapps3.db.FavoriteHelper
import de.hdodenhof.circleimageview.CircleImageView








lateinit var fcontext: Context
class FavoriteAdapter(private val listUser: ArrayList<Favorite>) : RecyclerView.Adapter<FavoriteAdapter.ListViewHolder>() {
    private lateinit var favoriteHelper: FavoriteHelper

    fun addItem(note: Favorite) {
        this.listUser.add(note)
        notifyItemInserted(this.listUser.size - 1)
    }

    fun updateItem(position: Int, note: Favorite) {
        this.listUser[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUser.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (position, username, avatar) = listUser[position]
        holder.tvUsername.text = username.toString()
        Glide.with(holder.itemView.context).load(avatar).into(holder.imgAvatar)

        holder.itemView.setOnClickListener(View.OnClickListener {
            val moveWithDataIntent = Intent(holder.itemView.context, DetailActivity::class.java)
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_NAME, username)
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_POSITION, position)
            holder.itemView.context.startActivity(moveWithDataIntent)
        })
    }
    override fun getItemCount(): Int = this.listUser.size

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.txt_username)
        var imgAvatar: CircleImageView = itemView.findViewById(R.id.img_avatar)
    }
}