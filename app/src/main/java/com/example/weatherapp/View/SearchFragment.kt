package com.example.weatherapp.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Model.HelperClass
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.SearchedCityAdapter
import com.example.weatherapp.ViewModel.SearchedCityViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var viewModel: SearchedCityViewModel

    private lateinit var myAdapter: SearchedCityAdapter
    private lateinit var myLayoutManager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private var cities: MutableCollection<String> = mutableListOf()

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

        viewModel = ViewModelProvider(requireActivity()).get(SearchedCityViewModel::class.java)

        myLayoutManager = LinearLayoutManager(context)
        myAdapter = SearchedCityAdapter(viewModel.searchedCities, viewModel)

        viewModel.searchedCities.observe(viewLifecycleOwner, Observer {
            myAdapter.notifyDataSetChanged()
        })

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val SearchButton = view.findViewById<Button>(R.id.searchedCityButton)
        val SearchedCitiesRV = view.findViewById<RecyclerView>(R.id.searchedCitiesRV)

        recyclerView = SearchedCitiesRV.apply {
            this.adapter = myAdapter
            this.layoutManager = myLayoutManager
        }

        // Dodanie reakcji na przycisk wyszukiwania
        // Po przyciśnięciu sprawdzane jest czy pobrany tekst nie jest pusty.
        // Jeśli nie to dodawany jest do bazy i do zmiennej globalnej a następnie przechodzi do odpowiedniej strony.
        SearchButton.setOnClickListener {
            val cityName = view.findViewById<TextView>(R.id.cityNameTV)

            if(!"".equals(cityName.getText().toString()))
            {
                viewModel.addCity(cityName.getText().toString())
                HelperClass.city = cityName.getText().toString()
                view.findNavController().navigate(R.id.action_mainFragment_to_searchedCityFragment)
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}