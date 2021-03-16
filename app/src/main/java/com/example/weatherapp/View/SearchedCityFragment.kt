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
import androidx.core.net.toUri
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

    private fun isAddedToFavorites(cityName: String): Boolean{
        var tmp: Int = 0
        for(city in viewModelFavCities.favoriteCities.value!!)
        {
            if(city.cityName.toLowerCase().equals(cityName.toLowerCase()))
                tmp += 1
        }

        return tmp > 0
    }

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(requireActivity()).get(StationViewModel::class.java)
        viewModelFavCities = ViewModelProvider(requireActivity()).get(FavoriteCitiesViewModel::class.java)

        viewModel.getSearchedStation(HelperClass.city)

        viewModel.searchedStation.observe(viewLifecycleOwner, Observer {

            val cityName = view?.findViewById<TextView>(R.id.searchedCityName)
            val temperatureDay = view?.findViewById<TextView>(R.id.temperatureDay)
            val weatherIconDesc = view?.findViewById<TextView>(R.id.weatherIconDescription)
            val temperatureTV = view?.findViewById<TextView>(R.id.temperatureTV)
            val sunriseTime = view?.findViewById<TextView>(R.id.sunriseTime)
            val sunsetTime = view?.findViewById<TextView>(R.id.sunsetTime)
            val pressureValue = view?.findViewById<TextView>(R.id.pressureValue)

            var iconUrl: String = "http://openweathermap.org/img/wn/"
            if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 200..299)
                iconUrl += "11"
            if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 300..321 && viewModel.searchedStation.value?.weather?.get(
                            0
                    )?.id!! in 520..531
            )
                iconUrl += "09"
            if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 500..504)
                iconUrl += "10"
            if (viewModel.searchedStation.value?.weather?.get(0)?.id!! in 600..622 && viewModel.searchedStation.value?.weather?.get(
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

            var imageView: ImageView? = view?.findViewById<ImageView>(R.id.weatherIcon)

            bindImage(imageView!!, iconUrl)

            var formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
            var formatter2 = DateTimeFormatter.ofPattern("yyyy-mm-dd HH:mm:ss")

            temperatureDay?.text = formatter2.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.dt!!), ZoneId.of("GMT+1")))
            cityName?.text = viewModel.searchedStation.value?.name
            temperatureTV?.text = viewModel.searchedStation.value?.main?.temp?.toInt().toString()
            weatherIconDesc?.text = viewModel.searchedStation.value?.weather?.get(0)?.description
            sunriseTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.sys?.sunrise!!), ZoneId.of("GMT+1")))
            sunsetTime?.text = formatter.format(LocalDateTime.ofInstant(Instant.ofEpochSecond(viewModel.searchedStation?.value?.sys?.sunset!!), ZoneId.of("GMT+1")))
            pressureValue?.text = viewModel.searchedStation.value?.main?.pressure?.toInt().toString() + "hPa"

            val addToFavoriteButton = view?.findViewById<CheckBox>(R.id.addToFavoriteButton)
            if(isAddedToFavorites(viewModel.searchedStation?.value?.name!!)){
                addToFavoriteButton?.setChecked(true)
                addToFavoriteButton?.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
            }
            else {
                addToFavoriteButton?.setChecked(false)
            }
            addToFavoriteButton?.toggle()

        })

        return inflater.inflate(R.layout.fragment_searched_city, container, false)
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val goBackBtn = view.findViewById<Button>(R.id.goBackButton)
        val sunriseIcon = view.findViewById<ImageView>(R.id.sunriseIcon)
        val sunsetIcon = view.findViewById<ImageView>(R.id.sunsetIcon)
        val pressureIcon = view.findViewById<ImageView>(R.id.pressureIcon)
        val addToFavoriteButton = view.findViewById<CheckBox>(R.id.addToFavoriteButton)

        addToFavoriteButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (addToFavoriteButton.isChecked == false)
            {
                viewModelFavCities.addCity(viewModel.searchedStation.value?.name!!)
                addToFavoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_24, 0, 0, 0)
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show()
            }
            if(addToFavoriteButton.isChecked == true){
                viewModelFavCities.deleteCity(viewModel.searchedStation.value?.name!!)
                addToFavoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_baseline_favorite_border_24, 0, 0, 0)
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
            }
        }

        sunriseIcon.setImageDrawable(requireContext().resources.getDrawable(
            context?.resources?.getIdentifier(
                "sunrise", "drawable", ContextUtils.getActivity(
                    context
                )?.packageName)!!
        ))

        sunsetIcon.setImageDrawable(requireContext().resources.getDrawable(
            context?.resources?.getIdentifier(
                "sunset", "drawable", ContextUtils.getActivity(
                    context
                )?.packageName)!!
        ))

        pressureIcon.setImageDrawable(requireContext().resources.getDrawable(
            context?.resources?.getIdentifier(
                "pressure", "drawable", ContextUtils.getActivity(
                    context
                )?.packageName)!!
        ))

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