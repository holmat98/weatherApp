package com.example.weatherapp.Model.Entities

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SearchedCityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(city: SearchedCity)

    @Delete
    suspend fun delete(city: SearchedCity)

    @Query("select * from searchedCities_table order by id desc limit 5")
    fun getAllSearchedCities() : LiveData<List<SearchedCity>>
}