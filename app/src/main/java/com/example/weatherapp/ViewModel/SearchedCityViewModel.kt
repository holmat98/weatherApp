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

// Klasa odpowiedzialna za komunikację z Api
class SearchedCityViewModel(application: Application) : AndroidViewModel(application) {
    val searchedCityRepository: SearchedCityRepository
    var searchedCities: LiveData<List<SearchedCity>>

    init {
        searchedCityRepository = SearchedCityRepository(WeatherDatabase.getDatabase(application).searchedCityDao())
        searchedCities = WeatherDatabase.getDatabase(application).searchedCityDao().getAllSearchedCities()
    }

    fun addCity(cityName: String){
        viewModelScope.launch {
            searchedCityRepository.insert(SearchedCity(id = 0, city = cityName))
        }
    }

    fun deleteCity(city: SearchedCity){
        viewModelScope.launch {
            searchedCityRepository.delete(city)
        }
    }
}