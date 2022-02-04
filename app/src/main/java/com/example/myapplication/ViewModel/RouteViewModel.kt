package com.example.myapplication.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.Model.RouteApiResponse
import com.example.myapplication.Repo.RouteRepo

class RouteViewModel(application: Application) : AndroidViewModel(application)  {

    lateinit var routeRepo: RouteRepo
    var routelivedata : LiveData<RouteApiResponse>? = null
    var initialize = false

    fun getroutedetails(url : String?){
        if (!initialize){
            routeRepo = RouteRepo()
            this.routelivedata = routeRepo.drawRoutes(url)
        }
    }

    fun getLiveData() : LiveData<RouteApiResponse>?{
        return routelivedata
    }
}