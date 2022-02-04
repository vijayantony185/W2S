package com.example.myapplication.Adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Model.PostsResponse
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.toColor
import com.example.myapplication.R
import com.example.myapplication.databinding.AdapterUserBinding
import kotlinx.android.synthetic.main.adapter_user.view.*


class UserAdapter(var context : Context,var model : ArrayList<PostsResponse>,var click : PostClick) : RecyclerView.Adapter<UserAdapter.UserHolder>() {

    inner class UserHolder(val binding : AdapterUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindview(postsResponse: PostsResponse) {
            binding.tvUserId.text = "User Id "+postsResponse.userId!!.toString()
            binding.tvTitle.text = postsResponse.title!!.toString()
            binding.totalLay.setOnClickListener {
                click.model(postsResponse)
            }
        }

    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): UserHolder {
        return UserHolder(AdapterUserBinding.inflate(LayoutInflater.from(p0.context),p0, false))
    }

    override fun onBindViewHolder(p0: UserHolder, p1: Int) {
        p0.bindview(model[p1])

        if (p1 % 2 == 0){
            p0.itemView.img_user.setColorFilter(ContextCompat.getColor(context, R.color.black));
        }else {
            p0.itemView.img_user.setColorFilter(ContextCompat.getColor(context, R.color.orange_dark));
        }
    }

    override fun getItemCount(): Int {
        return model.size
    }

    interface PostClick {
        fun model(postsResponse: PostsResponse)
    }
}