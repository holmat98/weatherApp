package com.example.weatherapp.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.weatherapp.Model.HelperClass
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.SearchedCityViewModel
import com.example.weatherapp.ViewModel.StationViewModel
import com.google.android.material.internal.ContextUtils
import java.text.SimpleDateFormat
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

    fun bindImage(imgView: ImageView, imgUrl: String?){
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        viewModel = ViewModelProvider(requireActivity()).get(StationViewModel::class.java)

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

            var jdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")
            jdf.timeZone = TimeZone.getTimeZone("GMT+1")
            var currentDate = jdf.format(Date(viewModel.searchedStation.value?.dt!!))
            var sunrise = jdf.format(Date(viewModel.searchedStation?.value?.sys?.sunrise!!))
            var sunset = jdf.format(Date(viewModel.searchedStation?.value?.sys?.sunset!!))

            iconUrl += if (currentDate >= sunrise && currentDate < sunset)
                "d.png"
            else
                "n.png"

            temperatureDay?.text = currentDate

            var jdf2 = SimpleDateFormat("HH:mm:ss z")
            jdf2.timeZone = TimeZone.getTimeZone("GMT+1")
            sunrise = jdf2.format(Date(viewModel.searchedStation?.value?.sys?.sunrise!!))
            sunset = jdf2.format(Date(viewModel.searchedStation?.value?.sys?.sunset!!))

            var imageView: ImageView? = view?.findViewById<ImageView>(R.id.weatherIcon)

            bindImage(imageView!!, iconUrl)

            cityName?.text = viewModel.searchedStation.value?.name
            temperatureTV?.text = viewModel.searchedStation.value?.main?.temp?.toInt().toString()
            weatherIconDesc?.text = viewModel.searchedStation.value?.weather?.get(0)?.description
            sunriseTime?.text = sunrise
            sunsetTime?.text = sunset
            pressureValue?.text = viewModel.searchedStation.value?.main?.pressure?.toInt().toString() + "hPa"

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