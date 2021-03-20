package com.example.weatherapp.View

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Model.Entities.Station
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.FavoriteCitiesViewModel
import com.example.weatherapp.ViewModel.FavoriteCityAdapter
import com.example.weatherapp.ViewModel.SearchedCityAdapter
import com.example.weatherapp.ViewModel.StationViewModel
import com.google.android.gms.location.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var stations: ArrayList<Station>

    private lateinit var myAdapter: FavoriteCityAdapter
    private lateinit var myLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private lateinit var viewModel: FavoriteCitiesViewModel
    private lateinit var viewModelStation: StationViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    val PERMISSION_ID = 1010

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation

            viewModelStation.getStationFromLocation(lastLocation.latitude, lastLocation.longitude)
        }
    }

    private fun checkPermission(): Boolean{
        return (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION_ID)
    }

    private fun isLocationEnabled(): Boolean{
        var locationManager: LocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager //getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener{
                    task -> var location: Location? = task.result
                    if(location == null) {
                        newLocationData()
                    }
                    else{
                        viewModelStation.getStationFromLocation(location.latitude, location.longitude)
                    }
                }
            }
            else{
                Toast.makeText(context, "Please turn on Your device location", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            requestPermission()
        }
    }

    private fun newLocationData(){
        var locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if(checkPermission())
            fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        stations = arrayListOf()
        stations.clear()

        viewModelStation = ViewModelProvider(requireActivity()).get(StationViewModel::class.java)

        myLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        myAdapter = FavoriteCityAdapter(stations, context)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        requestPermission()
        getLastLocation()

        viewModelStation.stationFromLocation.observe(viewLifecycleOwner, Observer {
            var numOfRepetitions: Int = 0
            for(j in stations){
                if(j.name.equals(viewModelStation.stationFromLocation.value?.name))
                    numOfRepetitions += 1
            }
            if(numOfRepetitions == 0) {
                stations.add(viewModelStation.stationFromLocation.value as Station)
                myAdapter.notifyDataSetChanged()
            }
        })

        viewModel = ViewModelProvider(requireActivity()).get(FavoriteCitiesViewModel::class.java)

        viewModel.favoriteCities.observe(viewLifecycleOwner, Observer {
            for (i in viewModel.favoriteCities.value!!){
                var numOfRepetitions: Int = 0
                for(j in stations){
                    if(j.name.equals(i.cityName))
                        numOfRepetitions += 1
                }
                if(numOfRepetitions == 0)
                    viewModelStation.getSearchedStation(i.cityName)
            }
        })

        viewModelStation.searchedStation.observe(viewLifecycleOwner, Observer {
            stations.add(viewModelStation.searchedStation.value as Station)
            myAdapter.notifyDataSetChanged()
        })

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteCitiesRV = view.findViewById<RecyclerView>(R.id.favoriteCitiesRV)

        recyclerView = favoriteCitiesRV.apply {
            this.adapter = myAdapter
            this.layoutManager = myLayoutManager
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}