package com.example.weatherapp.Model.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL="api.openweathermap.org/data/2.5/weather"

object Service{
    private val retrofit by lazy{
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val stationApi: StationApi by lazy {
        retrofit
                .create(StationApi::class.java)
    }
}