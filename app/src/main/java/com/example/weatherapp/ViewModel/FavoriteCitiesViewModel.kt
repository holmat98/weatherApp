package com.example.weatherapp.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Model.Entities.FavoriteCity
import com.example.weatherapp.Model.Entities.SearchedCity
import com.example.weatherapp.Model.Repositories.FavoriteCityRepository
import com.example.weatherapp.Model.Repositories.SearchedCityRepository
import com.example.weatherapp.Model.WeatherDatabase
import kotlinx.coroutines.launch

class FavoriteCitiesViewModel(application: Application) : AndroidViewModel(application) {
    val favoriteCitiesRepository: FavoriteCityRepository
    var favoriteCities: LiveData<List<FavoriteCity>>

    init {
        favoriteCitiesRepository = FavoriteCityRepository(WeatherDatabase.getDatabase(application).favoriteCityDao())
        favoriteCities = WeatherDatabase.getDatabase(application).favoriteCityDao().getAllFavoritesCities()
    }

    fun addCity(cityName: String){
        viewModelScope.launch {
            favoriteCitiesRepository.insert(FavoriteCity(id=0, cityName = cityName))
        }
    }

    fun delete(city: FavoriteCity){
        viewModelScope.launch {
            favoriteCitiesRepository.delete(city)
        }
    }

    fun deleteCity(cityName: String){
        viewModelScope.launch {
            favoriteCitiesRepository.deleteCity(cityName)
        }
    }
}