package com.example.myapplication.Retrofit

import com.example.myapplication.Model.PostsResponse
import com.example.myapplication.Model.RouteApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface ApiInterface {
    @Headers("Content-Type: application/json")
    @GET
    fun getposts(@Url url: String): retrofit2.Call<ArrayList<PostsResponse>>

    @Headers("Content-Type: application/json")
    @GET
    fun getdata(@Url url: String): retrofit2.Call<RouteApiResponse>
}