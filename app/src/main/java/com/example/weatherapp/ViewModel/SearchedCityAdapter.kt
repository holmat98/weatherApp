package com.example.weatherapp.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Model.Entities.SearchedCity
import com.example.weatherapp.R

class SearchedCityAdapter(var cities: LiveData<List<SearchedCity>>, val viewModel: SearchedCityViewModel): RecyclerView.Adapter<SearchedCityAdapter.CitiesHolder>() {
    inner class CitiesHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.searched_city_one_row, parent, false)
        return CitiesHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        val searchedCityName = holder.itemView.findViewById<TextView>(R.id.searchedCityTV)
        val deleteBtn = holder.itemView.findViewById<Button>(R.id.deleteSearchedCityBtn)

        searchedCityName.text = cities.value?.get(position)?.city

        deleteBtn.setOnClickListener {
            viewModel.deleteCity(cities.value?.get(position)!!)
        }
    }

    override fun getItemCount(): Int {
        return cities.value?.size?: 0
    }


}