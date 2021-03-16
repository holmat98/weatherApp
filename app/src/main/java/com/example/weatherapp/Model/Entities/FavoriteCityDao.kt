package com.example.weatherapp.Model.Entities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: FavoriteCity)

    @Delete
    suspend fun delete(city: FavoriteCity)

    @Query("select * from favoriteCity_table")
    fun getAllFavoritesCities() : LiveData<List<FavoriteCity>>

    @Query("delete from favoriteCity_table where cityName=:cityName")
    suspend fun deleteCity(cityName: String)
}