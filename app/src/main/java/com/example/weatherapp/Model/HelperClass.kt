package com.example.weatherapp.Model

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherapp.Model.Entities.FavoriteCity
import com.example.weatherapp.View.HomeFragment

class HelperClass {
    companion object{
        var city: String = ""
        var currentFragment: Fragment = HomeFragment.newInstance("","")
        var favoriteCities: LiveData<List<FavoriteCity>>? = null
        var layoutForElderly: Boolean = false
    }
}