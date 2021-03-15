package com.example.weatherapp.Model.API

import androidx.lifecycle.LiveData
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.weatherapp.Model.Entities.Station
import retrofit2.Call

interface StationApi {

    @GET("data/2.5/weather/")
    fun getStation(@Query("q") city: String, @Query("units") units: String = "Metric", @Query("appid") key: String = "apiKey") : Call<Station>
}