package com.example.weatherapp.ViewModel

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.Model.Entities.SearchedCity
import com.example.weatherapp.Model.Entities.Station
import com.example.weatherapp.Model.HelperClass
import com.example.weatherapp.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FavoriteCityAdapter(var cities: ArrayList<Station>): RecyclerView.Adapter<FavoriteCityAdapter.CitiesHolder>() {
    inner class CitiesHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_city_one_row, parent, false)
        return CitiesHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        val cityName = holder.itemView.findViewById<TextView>(R.id.searchedCityName2)
        val temperatureDay = holder.itemView.findViewById<TextView>(R.id.temperatureDay2)
        val weatherIconDesc = holder.itemView.findViewById<TextView>(R.id.weatherIconDescription2)
        val temperatureTV = holder.itemView.findViewById<TextView>(R.id.temperatureTV2)
        val sunriseTime = holder.itemView.findViewById<TextView>(R.id.sunriseTime2)
        val sunsetTime = holder.itemView.findViewById<TextView>(R.id.sunsetTime2)
        val pressureValue = holder.itemView.findViewById<TextView>(R.id.pressureValue2)

        var iconUrl: String = "http://openweathermap.org/img/wn/"
        if (cities.get(position)?.weather?.get(0)?.id!! in 200..299)
            iconUrl += "11"
        if (cities.get(position)?.weather?.get(0)?.id!! in 300..321 && cities.get(position)?.weather?.get(
                0
            )?.id!! in 520..531
        )
            iconUrl += "09"
        if (cities.get(position)?.weather?.get(0)?.id!! in 500..504)
            iconUrl += "10"
        if (cities.get(position)?.weather?.get(0)?.id!! in 600..622 && cities.get(position)?.weather?.get(
                0
            )?.id!! == 511.toLong()
        )
            iconUrl += "13"
        if (cities.get(position)?.weather?.get(0)?.id!! in 700..799)
            iconUrl += "50"
        if (cities.get(position)?.weather?.get(0)?.id!! == 800.toLong())
            iconUrl += "01"
        if (cities.get(position)?.weather?.get(0)?.id!! == 801.toLong())
            iconUrl += "02"
        if (cities.get(position)?.weather?.get(0)?.id!! == 802.toLong())
            iconUrl += "03"
        if (cities.get(position)?.weather?.get(0)?.id!! in 803..804)
            iconUrl += "04"

        var currentDate = LocalDateTime.now(ZoneOffset.UTC)
        var currentDateUnix = currentDate.atZone(ZoneOffset.UTC).toEpochSecond()

        iconUrl += if(currentDateUnix >= cities.get(position)?.sys?.sunset!!)
            "n.png"
        else if(currentDateUnix < cities.get(position)?.sys?.sunset!! && currentDateUnix >= cities.get(position)?.sys?.sunrise!!)
            "d.png"
        else
            "n.png"

        var imageView: ImageView = holder.itemView.findViewById<ImageView>(R.id.weatherIcon2)

        bindImage(imageView, iconUrl)

        var formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        var formatter2 = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss")

        temperatureDay?.text = formatter2.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(cities.get(position)?.dt!!), ZoneId.of("GMT+1")))
        cityName?.text = cities.get(position)?.name
        temperatureTV?.text = cities.get(position)?.main?.temp?.toInt().toString()
        weatherIconDesc?.text = cities.get(position)?.weather?.get(0)?.description
        sunriseTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(cities.get(position)?.sys?.sunrise!!), ZoneId.of("GMT+1")))
        sunsetTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(cities.get(position)?.sys?.sunset!!), ZoneId.of("GMT+1")))
        pressureValue?.text = cities.get(position)?.main?.pressure?.toInt().toString() + "hPa"
    }

    override fun getItemCount(): Int {
        return cities.size?: 0
    }

    private fun bindImage(imgView: ImageView, imgUrl: String?){
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                .load(imgUri)
                .into(imgView)
        }
    }
}