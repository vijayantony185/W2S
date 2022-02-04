package com.example.myapplication.Repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Model.PostsResponse
import com.example.myapplication.Retrofit.ApiInterface
import com.example.myapplication.Retrofit.RetrofitRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostsRepo {

    var apiRequest : ApiInterface

    init {
        apiRequest = RetrofitRequest.getRetrofitInstance()!!.create(ApiInterface::class.java)
    }

    fun getPosts(url : String?) : LiveData<ArrayList<PostsResponse>>{
        val data: MutableLiveData<ArrayList<PostsResponse>> = MutableLiveData()
        apiRequest.getposts(url!!).enqueue(object : Callback<ArrayList<PostsResponse>>{
            override fun onResponse(p0: Call<ArrayList<PostsResponse>>, p1: Response<ArrayList<PostsResponse>>) {
                if (p1.body() != null) {
                    data.setValue(p1.body())
                    Log.d("Status succes","------------------->success")

                }else{
                    Log.d("Status failed","------------------->${p1.code()}")

                }
            }

            override fun onFailure(p0: Call<ArrayList<PostsResponse>>, p1: Throwable) {
                Log.d("Status Failed","------------------->${p1.message}")
                data.setValue(null)
            }

        })
        return data
    }
}