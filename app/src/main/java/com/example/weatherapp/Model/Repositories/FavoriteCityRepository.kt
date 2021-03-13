package com.example.weatherapp.Model.Repositories

import androidx.lifecycle.LiveData
import com.example.weatherapp.Model.Entities.FavoriteCity
import com.example.weatherapp.Model.Entities.FavoriteCityDao

class FavoriteCityRepository(val favoriteCityDao: FavoriteCityDao) {
    suspend fun insert(city: FavoriteCity){
        favoriteCityDao.insert(city)
    }

    suspend fun delete(city: FavoriteCity){
        favoriteCityDao.delete(city)
    }

    fun getAllCities() : LiveData<List<FavoriteCity>> = favoriteCityDao.getAllFavoritesCities()

}