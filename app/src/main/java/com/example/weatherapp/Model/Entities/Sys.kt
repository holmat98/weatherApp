package com.example.weatherapp.Model.Entities

data class Sys(
    val type: Int,
    val id: Long,
    val country: String,
    val sunrise: Long,
    val sunset: Long
) {
}