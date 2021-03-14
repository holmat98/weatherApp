package com.example.weatherapp.Model

import androidx.fragment.app.Fragment
import com.example.weatherapp.View.HomeFragment

class HelperClass {
    companion object{
        var city: String = ""
        var currentFragment: Fragment = HomeFragment.newInstance("","")
    }
}