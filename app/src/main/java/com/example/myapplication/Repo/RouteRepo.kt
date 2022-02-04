package com.example.myapplication.Repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.Model.RouteApiResponse
import com.example.myapplication.Retrofit.ApiInterface
import com.example.myapplication.Retrofit.DirectionsRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteRepo {
    lateinit var apiRequest : ApiInterface

    init {
        apiRequest = DirectionsRequest.getRetrofitInstance()!!.create(ApiInterface::class.java)
    }

    fun drawRoutes(url : String?) : LiveData<RouteApiResponse>{
        val data: MutableLiveData<RouteApiResponse> = MutableLiveData()
        apiRequest.getdata(url!!).enqueue(object : Callback<RouteApiResponse> {
            override fun onResponse(
                call: Call<RouteApiResponse>,
                response: Response<RouteApiResponse>
            ) {
                if (response.body() != null) {
                    data.setValue(response.body())
                    Log.d("Status succes","------------------->success")

                }else{
                    Log.d("Status failed","------------------->${response.code()}")

                }
            }

            override fun onFailure(call: Call<RouteApiResponse>, t: Throwable) {
                Log.d("Status Failed","------------------->${t.message}")
                data.setValue(null)
            }

        })
        return data
    }
}