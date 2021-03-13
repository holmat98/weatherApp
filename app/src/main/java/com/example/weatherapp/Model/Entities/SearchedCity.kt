package com.example.weatherapp.Model.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchedCities_table")
data class SearchedCity(@PrimaryKey(autoGenerate = true) val id: Long, var city: String) {
}