package com.example.myapplication.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.Model.PostsResponse
import com.example.myapplication.Repo.PostsRepo

class PostsViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var postsRepo: PostsRepo
    var postlivedata : LiveData<ArrayList<PostsResponse>>? = null
    var initialize = false

    fun getposts(url : String?){
        if (!initialize){
            postsRepo = PostsRepo()
            postlivedata = postsRepo.getPosts(url)
        }
    }

    fun getLiveData() : LiveData<ArrayList<PostsResponse>>?{
        return postlivedata
    }
}