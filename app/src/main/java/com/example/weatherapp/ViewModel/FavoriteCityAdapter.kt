package com.example.weatherapp.ViewModel

import android.annotation.SuppressLint
import android.content.Context
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
import com.google.android.material.internal.ContextUtils
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class FavoriteCityAdapter(var cities: ArrayList<Station>, val context: Context?): RecyclerView.Adapter<FavoriteCityAdapter.CitiesHolder>() {
    inner class CitiesHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_city_one_row, parent, false)
        return CitiesHolder(view)
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CitiesHolder, position: Int) {
        val cityName = holder.itemView.findViewById<TextView>(R.id.searchedCityName2)
        val temperatureDay = holder.itemView.findViewById<TextView>(R.id.temperatureDay2)
        val weatherIconDesc = holder.itemView.findViewById<TextView>(R.id.weatherDescription2)
        val weatherFeels = holder.itemView.findViewById<TextView>(R.id.weatherFeels2)
        val temperatureTV = holder.itemView.findViewById<TextView>(R.id.temperatureTV2)
        val sunriseTime = holder.itemView.findViewById<TextView>(R.id.sunriseValue2)
        val sunsetTime = holder.itemView.findViewById<TextView>(R.id.sunsetValue2)
        val windValue = holder.itemView.findViewById<TextView>(R.id.windValue2)
        val humidityValue = holder.itemView.findViewById<TextView>(R.id.humidityValue2)
        val pressureValue = holder.itemView.findViewById<TextView>(R.id.pressureValue2)
        val backgroundImageView = holder.itemView.findViewById<ImageView>(R.id.backgroundImage2)

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

        var backgroundImage: String = ""

        if (cities.get(position)?.weather?.get(0)?.id!! in 200..299)
            backgroundImage = "rainy.jpeg"
        if (cities.get(position)?.weather?.get(0)?.id!! in 300..321 && cities.get(position)?.weather?.get(
                        0
                )?.id!! in 520..531
        )
            backgroundImage = "rainy"
        if (cities.get(position)?.weather?.get(0)?.id!! in 500..504)
            backgroundImage = "sunny"
        if (cities.get(position)?.weather?.get(0)?.id!! in 600..622 && cities.get(position)?.weather?.get(
                        0
                )?.id!! == 511.toLong()
        )
            backgroundImage = "snowy"
        if (cities.get(position)?.weather?.get(0)?.id!! in 700..799)
            backgroundImage = "foggy"
        if (cities.get(position)?.weather?.get(0)?.id!! == 800.toLong())
            backgroundImage = "sunny"
        if (cities.get(position)?.weather?.get(0)?.id!! == 801.toLong())
            backgroundImage = "sunny"
        if (cities.get(position)?.weather?.get(0)?.id!! == 802.toLong())
            backgroundImage = "cloudy"
        if (cities.get(position)?.weather?.get(0)?.id!! in 803..804)
            backgroundImage = "cloudy"

        backgroundImageView.setImageDrawable(context!!.resources.getDrawable( context.resources.getIdentifier(backgroundImage, "drawable", ContextUtils.getActivity(
                context
        )?.packageName)))

        var formatter = DateTimeFormatter.ofPattern("HH:mm")
        var formatter2 = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm")

        temperatureDay?.text = formatter2.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(cities.get(position)?.dt!!), ZoneId.of("GMT+1")))
        cityName?.text = cities.get(position)?.name
        temperatureTV?.text = cities.get(position)?.main?.temp?.toInt().toString()
        weatherIconDesc?.text = cities.get(position)?.weather?.get(0)?.description
        weatherFeels?.text = cities.get(position)?.main?.temp_min.toString() + "/" + cities.get(position)?.main?.temp_max.toString() + " feels like " +cities.get(position)?.main?.feels_like.toString()
        sunriseTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(cities.get(position)?.sys?.sunrise!!), ZoneId.of("GMT+1")))
        sunsetTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(cities.get(position)?.sys?.sunset!!), ZoneId.of("GMT+1")))
        pressureValue?.text = cities.get(position)?.main?.pressure?.toInt().toString() + "hPa"
        windValue?.text = cities.get(position)?.wind?.speed.toString() + "m/s"
        humidityValue?.text = cities.get(position)?.main?.humidity.toString() + "%"
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