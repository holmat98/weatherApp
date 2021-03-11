package com.example.weatherapp.ViewModel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R

class SearchedCityAdapter(val cities: MutableCollection<String>): RecyclerView.Adapter<SearchedCityAdapter.CitiesHolder>() {
    inner class CitiesHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.searched_city_one_row, parent, false)
        return CitiesHolder(view)
    }

    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        val searchedCityName = holder.itemView.findViewById<TextView>(R.id.searchedCityTV)
        val deleteBtn = holder.itemView.findViewById<Button>(R.id.deleteSearchedCityBtn)

        searchedCityName.text = cities.elementAt(position)

        deleteBtn.setOnClickListener {
            cities.remove(cities.elementAt(position))
            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }


}