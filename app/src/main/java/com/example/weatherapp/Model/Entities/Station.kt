package com.example.weatherapp.Model.Entities

data class Station(
    val coord: Coordinations,
    val weather: ArrayList<Weather>,
    val base: String,
    val main: Temperature,
    val visibility: Double,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Long
) {
}