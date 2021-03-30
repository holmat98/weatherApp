package com.example.weatherapp.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.Model.HelperClass
import com.example.weatherapp.R
import com.example.weatherapp.ViewModel.FavoriteCitiesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val homeFragment: HomeFragment = HomeFragment.newInstance("", "")
    private val searchFragment: SearchFragment = SearchFragment.newInstance("", "")
    private val settingsFragment: SettingsFragment = SettingsFragment.newInstance("", "")

    private lateinit var viewModel: FavoriteCitiesViewModel

    private fun makeCurrentFragment(fragment: Fragment) =
            activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.fragmentViewer, fragment)
            ?.commit()


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

        viewModel = ViewModelProvider(requireActivity()).get(FavoriteCitiesViewModel::class.java)

        viewModel.favoriteCities.observe(viewLifecycleOwner, Observer {
            HelperClass.favoriteCities = viewModel.favoriteCities
        })

        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bttnNav = view.findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Wyświeltenie strony domowej jako głównej
        makeCurrentFragment(HelperClass.currentFragment)


        // Zamiana wyświetlanego fragmentu po naciśnięciu przycisku w menu
        bttnNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> {
                    makeCurrentFragment(homeFragment)
                    HelperClass.currentFragment = homeFragment
                }
                R.id.ic_search -> {
                    makeCurrentFragment(searchFragment)
                    HelperClass.currentFragment = searchFragment
                }
                R.id.ic_settings -> {
                    makeCurrentFragment(settingsFragment)
                    HelperClass.currentFragment = settingsFragment
                }
            }
            true
        }


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MainFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}