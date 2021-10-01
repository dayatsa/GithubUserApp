package com.example.githubuserapps3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserapps3.DetailActivity
import com.example.githubuserapps3.R
import com.example.githubuserapps3.data.User
import de.hdodenhof.circleimageview.CircleImageView

class FollowersAdapter(private val listUser: ArrayList<User>) : RecyclerView.Adapter<FollowersAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvUsername: TextView = itemView.findViewById(R.id.txt_username)
        var imgAvatar: CircleImageView = itemView.findViewById(R.id.img_avatar)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false)
        mcontext = viewGroup.context
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, name, avatar, company, location, repository, followers, following) = listUser[position]
        holder.tvUsername.text = username
        Glide.with(holder.itemView.context).load(avatar).into(holder.imgAvatar)
        holder.itemView.setOnClickListener(View.OnClickListener {
            val moveWithDataIntent = Intent(mcontext, DetailActivity::class.java)
            moveWithDataIntent.putExtra(DetailActivity.EXTRA_NAME, username)
            mcontext.startActivity(moveWithDataIntent)
        })
    }

    override fun getItemCount(): Int = listUser.size
}