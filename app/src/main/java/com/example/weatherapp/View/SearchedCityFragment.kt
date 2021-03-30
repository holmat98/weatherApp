package com.example.weatherapp.View

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.solver.widgets.Helper
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.weatherapp.Model.HelperClass
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.FavoriteCitiesViewModel
import com.example.weatherapp.ViewModel.SearchedCityViewModel
import com.example.weatherapp.ViewModel.StationViewModel
import com.google.android.material.internal.ContextUtils
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchedCityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchedCityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: StationViewModel
    private lateinit var viewModelFavCities: FavoriteCitiesViewModel

    // Funkcja sprawdza czy wyszukane miasto jest dodane do ulubionych
    private fun isAddedToFavorites(cityName: String): Boolean{
        var tmp: Int = 0
        for(city in viewModelFavCities.favoriteCities.value!!)
        {
            if(city.cityName.toLowerCase().equals(cityName.toLowerCase()))
                tmp += 1
        }

        return tmp > 0
    }

    // Funkcja pobiera ikonę pogody ze strony Api i przypisuje do imageView
    private fun bindImage(imgView: ImageView, imgUrl: String?){
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            Glide.with(imgView.context)
                    .load(imgUri)
                    .into(imgView)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // Wysyłane jest zapytanie do Api. Po otrzymaniu odpowiedzi, dane pogodowe są przypisywane do odpowiednich elementów layoutu
    // w zależności od tego czy jest włączony tryb dla osób starszych
    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(requireActivity()).get(StationViewModel::class.java)
        viewModelFavCities = ViewModelProvider(requireActivity()).get(FavoriteCitiesViewModel::class.java)

        viewModel.getSearchedStation(HelperClass.city)

        viewModel.searchedStation.observe(viewLifecycleOwner, Observer {
            val cityName = view?.findViewById<TextView>(R.id.searchedCityName)
            cityName?.text = viewModel.searchedStation.value?.name

            if(HelperClass.layoutForElderly){
                val temperatureDay = view?.findViewById<TextView>(R.id.temperatureDay3)
                val weatherIconDesc = view?.findViewById<TextView>(R.id.weatherDescription3)
                val weatherFeels = view?.findViewById<TextView>(R.id.weatherFeels3)
                val temperatureTV = view?.findViewById<TextView>(R.id.temperatureTV3)
                val sunriseTime = view?.findViewById<TextView>(R.id.sunriseValue3)
                val sunsetTime = view?.findViewById<TextView>(R.id.sunsetValue3)
                val windValue = view?.findViewById<TextView>(R.id.windValue3)
                val humidityValue = view?.findViewById<TextView>(R.id.humidityValue3)
                val pressureValue = view?.findViewById<TextView>(R.id.pressureValue3)
                val backgroundImageView = view?.findViewById<ImageView>(R.id.backgroundImage)

                val normalLayout = view?.findViewById<ScrollView>(R.id.normalLayout)
                val layoutForElderly = view?.findViewById<ScrollView>(R.id.layoutForElderly)

                normalLayout?.isVisible = false
                layoutForElderly?.isVisible = true
                backgroundImageView?.isVisible = false

                // Tworzenie odpowiedniego linka do pobrania zdjęcia
                var iconUrl: String = "http://openweathermap.org/img/wn/"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 200..299)
                    iconUrl += "11"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 300..321 || viewModel.searchedStation.value?.weather?.get(
                                0
                        )?.id!! in 520..531
                )
                    iconUrl += "09"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 500..504)
                    iconUrl += "10"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 600..622 || viewModel.searchedStation.value?.weather?.get(
                                0
                        )?.id!! == 511.toLong()
                )
                    iconUrl += "13"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 700..799)
                    iconUrl += "50"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 800.toLong())
                    iconUrl += "01"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 801.toLong())
                    iconUrl += "02"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 802.toLong())
                    iconUrl += "03"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 803..804)
                    iconUrl += "04"

                var currentDate = LocalDateTime.now(ZoneOffset.UTC)
                var currentDateUnix = currentDate.atZone(ZoneOffset.UTC).toEpochSecond()

                iconUrl += if(currentDateUnix >= viewModel.searchedStation?.value?.sys?.sunset!!)
                    "n.png"
                else if(currentDateUnix < viewModel.searchedStation?.value?.sys?.sunset!! && currentDateUnix >= viewModel.searchedStation?.value?.sys?.sunrise!!)
                    "d.png"
                else
                    "n.png"

                var imageView: ImageView? = view?.findViewById<ImageView>(R.id.weatherIcon3)

                bindImage(imageView!!, iconUrl)

                // Tworzenie formatu czasowego dla danych pobranych z Api
                var formatter = DateTimeFormatter.ofPattern("HH:mm")
                var formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                temperatureDay?.text = formatter2.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.dt!!), ZoneId.of("GMT+1")))
                temperatureTV?.text = viewModel.searchedStation.value?.main?.temp?.toInt().toString() + "\u2103"
                weatherIconDesc?.text = viewModel.searchedStation.value?.weather?.get(0)?.description
                weatherFeels?.text = viewModel.searchedStation.value?.main?.temp_min.toString() + "/" + viewModel.searchedStation.value?.main?.temp_max.toString() + " feels like " + viewModel.searchedStation.value?.main?.feels_like.toString()
                sunriseTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.sys?.sunrise!!), ZoneId.of("GMT+1")))
                sunsetTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.sys?.sunset!!), ZoneId.of("GMT+1")))
                pressureValue?.text = viewModel.searchedStation.value?.main?.pressure?.toInt().toString() + "hPa"
                windValue?.text = viewModel.searchedStation.value?.wind?.speed.toString() + "m/s"
                humidityValue?.text = viewModel.searchedStation.value?.main?.humidity.toString() + "%"
            }
            else{
                val temperatureDay = view?.findViewById<TextView>(R.id.temperatureDay)
                val weatherIconDesc = view?.findViewById<TextView>(R.id.weatherDescription)
                val weatherFeels = view?.findViewById<TextView>(R.id.weatherFeels)
                val temperatureTV = view?.findViewById<TextView>(R.id.temperatureTV)
                val sunriseTime = view?.findViewById<TextView>(R.id.sunriseValue)
                val sunsetTime = view?.findViewById<TextView>(R.id.sunsetValue)
                val windValue = view?.findViewById<TextView>(R.id.windValue)
                val humidityValue = view?.findViewById<TextView>(R.id.humidityValue)
                val pressureValue = view?.findViewById<TextView>(R.id.pressureValue)
                val backgroundImageView = view?.findViewById<ImageView>(R.id.backgroundImage)
                val normalLayout = view?.findViewById<ScrollView>(R.id.normalLayout)
                val layoutForElderly = view?.findViewById<ScrollView>(R.id.layoutForElderly)

                normalLayout?.isVisible = true
                layoutForElderly?.isVisible = false
                backgroundImageView?.isVisible = true

                var iconUrl: String = "http://openweathermap.org/img/wn/"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 200..299)
                    iconUrl += "11"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 300..321 || viewModel.searchedStation.value?.weather?.get(
                                0
                        )?.id!! in 520..531
                )
                    iconUrl += "09"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 500..504)
                    iconUrl += "10"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 600..622 || viewModel.searchedStation.value?.weather?.get(
                                0
                        )?.id!! == 511.toLong()
                )
                    iconUrl += "13"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 700..799)
                    iconUrl += "50"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 800.toLong())
                    iconUrl += "01"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 801.toLong())
                    iconUrl += "02"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 802.toLong())
                    iconUrl += "03"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 803..804)
                    iconUrl += "04"


                // Ustalanie, którego zdjęcia w tle użyć na podstawie danych pobranych z Api
                var backgroundImage: String = "clear"

                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 200..299)
                    backgroundImage = "thunderstorm"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 300..321)
                    backgroundImage = "rain"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 500..531)
                    backgroundImage = "rain"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 600..622)
                    backgroundImage = "snow"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 700..799)
                    backgroundImage = "fogg"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 800.toLong())
                    backgroundImage = "clear"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 801.toLong())
                    backgroundImage = "clear"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! == 802.toLong())
                    backgroundImage = "cloud"
                if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 803..804)
                    backgroundImage = "cloud"

                backgroundImageView?.setImageDrawable(context?.resources?.getDrawable( context?.resources!!.getIdentifier(backgroundImage, "drawable", ContextUtils.getActivity(
                        context
                )?.packageName)))

                var currentDate = LocalDateTime.now(ZoneOffset.UTC)
                var currentDateUnix = currentDate.atZone(ZoneOffset.UTC).toEpochSecond()

                iconUrl += if(currentDateUnix >= viewModel.searchedStation?.value?.sys?.sunset!!)
                    "n.png"
                else if(currentDateUnix < viewModel.searchedStation?.value?.sys?.sunset!! && currentDateUnix >= viewModel.searchedStation?.value?.sys?.sunrise!!)
                    "d.png"
                else
                    "n.png"

                var imageView: ImageView? = view?.findViewById<ImageView>(R.id.weatherIcon)

                bindImage(imageView!!, iconUrl)

                var formatter = DateTimeFormatter.ofPattern("HH:mm")
                var formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                temperatureDay?.text = formatter2.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.dt!!), ZoneId.of("GMT+1")))
                temperatureTV?.text = viewModel.searchedStation.value?.main?.temp?.toInt().toString() + "\u2103"
                weatherIconDesc?.text = viewModel.searchedStation.value?.weather?.get(0)?.description
                weatherFeels?.text = viewModel.searchedStation.value?.main?.temp_min.toString() + "\u2103" + "/" + viewModel.searchedStation.value?.main?.temp_max.toString() + "\u2103" + " feels like " + viewModel.searchedStation.value?.main?.feels_like.toString() + "\u2103"
                sunriseTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.sys?.sunrise!!), ZoneId.of("GMT+1")))
                sunsetTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.sys?.sunset!!), ZoneId.of("GMT+1")))
                pressureValue?.text = viewModel.searchedStation.value?.main?.pressure?.toInt().toString() + "hPa"
                windValue?.text = viewModel.searchedStation.value?.wind?.speed.toString() + "m/s"
                humidityValue?.text = viewModel.searchedStation.value?.main?.humidity.toString() + "%"
            }

            val addToFavoriteButton = view?.findViewById<Button>(R.id.addToFavoriteButton)
            if(isAddedToFavorites(viewModel.searchedStation?.value?.name!!)){
                addToFavoriteButton?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
            }
            else {
                addToFavoriteButton?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border_24, 0, 0, 0)
            }

        })

        return inflater.inflate(R.layout.fragment_searched_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goBackBtn = view.findViewById<Button>(R.id.goBackButton)
        val addToFavoriteButton = view.findViewById<Button>(R.id.addToFavoriteButton)

        // Ustawienie reakcji na przyciśnięcie przycisku. Przycisk dodaje lub usuwa z ulubionych
        addToFavoriteButton.setOnClickListener {
            if (!isAddedToFavorites(viewModel.searchedStation.value?.name!!))
            {
                viewModelFavCities.addCity(viewModel.searchedStation.value?.name!!)
                viewModelFavCities.getCities()
                addToFavoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
        }

        // Dodanie reakcji na przycisk cofania
        goBackBtn.setOnClickListener {
            view -> view.findNavController().navigate(R.id.action_searchedCityFragment_to_mainFragment)
            HelperClass.currentFragment = SearchFragment.newInstance("","")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchedCityFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SearchedCityFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}