package com.example.weatherapp.Model.Entities

data class Weather(
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
) {
}