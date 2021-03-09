package com.example.weatherapp.ViewModel

import android.app.Application
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

    fun getSearchedStation(city: String){
        viewModelScope.launch {
            _searchedStation.value = StationRepository.getAllStations(city = city)
        }
    }
}