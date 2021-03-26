package com.example.weatherapp.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherapp.Model.Entities.*

@Database(entities = [FavoriteCity::class, SearchedCity::class], version = 4, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase(){
    abstract fun searchedCityDao(): SearchedCityDao
    abstract fun favoriteCityDao(): FavoriteCityDao

    companion object{
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase{
            var tempInstance = INSTANCE

            if(tempInstance != null)
            {
                return tempInstance
            }
            else
            {
                synchronized(this){
                    val instance = Room.databaseBuilder(
                            context.applicationContext,
                            WeatherDatabase::class.java,
                            "weatherDatabase"
                    ).fallbackToDestructiveMigration().allowMainThreadQueries()
                            .build()
                    INSTANCE = instance
                    return instance
                }
            }
        }
    }
}