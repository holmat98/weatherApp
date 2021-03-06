package com.example.weatherapp.Model.Repositories

import com.example.weatherapp.Model.API.Service
import com.example.weatherapp.Model.Entities.Station
import retrofit2.awaitResponse

class StationRepository {
    companion object {
        suspend fun getAllGames(city: String): Station? {
            return Service.stationApi.getStation(city = city).awaitResponse().body()
        }
    }
}