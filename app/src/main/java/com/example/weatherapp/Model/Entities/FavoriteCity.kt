package com.example.weatherapp.Model.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoriteCity_table")
data class FavoriteCity(@PrimaryKey(autoGenerate = true) val id: Long, var cityName: String) {
}