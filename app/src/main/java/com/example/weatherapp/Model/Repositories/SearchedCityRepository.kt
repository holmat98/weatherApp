package com.example.weatherapp.Model.Repositories

import androidx.lifecycle.LiveData
import com.example.weatherapp.Model.Entities.FavoriteCity
import com.example.weatherapp.Model.Entities.SearchedCity
import com.example.weatherapp.Model.Entities.SearchedCityDao

class SearchedCityRepository(val searchedCityDao: SearchedCityDao) {
    suspend fun insert(city:SearchedCity){
        searchedCityDao.insert(city)
    }

    suspend fun delete(city:SearchedCity){
        searchedCityDao.delete(city)
    }

    fun getAllCities() : LiveData<List<SearchedCity>> = searchedCityDao.getAllSearchedCities()
}