package com.example.weatherapp.ViewModel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Model.Entities.Station
import com.example.weatherapp.Model.Repositories.StationRepository
import kotlinx.coroutines.launch

class StationViewModel(application: Application): AndroidViewModel(application){
    private var _searchedStation: MutableLiveData<Station?> = MutableLiveData()
    val searchedStation: LiveData<Station?>
    get() = _searchedStation

    private var _stationFromLocation: MutableLiveData<Station?> = MutableLiveData()
    val stationFromLocation: LiveData<Station?>
    get() = _stationFromLocation

    private var _favoriteStations: MutableLiveData<ArrayList<Station>> = MutableLiveData()
    val favoriteStations: LiveData<ArrayList<Station>>
    get() = _favoriteStations

    fun getSearchedStation(city: String){
        viewModelScope.launch {
            _searchedStation.value = StationRepository.getAllStations(city = city)
        }
    }

    fun getStationFromLocation(lat: Double, lon: Double){
        viewModelScope.launch {
            _stationFromLocation.value = StationRepository.getStationByLocation(lon = lon, lat = lat)
        }
    }
}