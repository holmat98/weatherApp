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
}