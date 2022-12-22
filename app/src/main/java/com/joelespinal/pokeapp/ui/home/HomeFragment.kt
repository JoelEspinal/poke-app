package com.joelespinal.pokeapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var categoriesSpinner: Spinner

    private lateinit var regionsLiveData: LiveData<List<String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        regionsLiveData = homeViewModel.regionsName
        homeViewModel.getRegionNames()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesSpinner = view.findViewById(R.id.categories_spinner)

      context?.let { context ->
          var regionsAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, mutableListOf<String>())
            regionsAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            regionsLiveData.observe( viewLifecycleOwner) { regions ->
                regionsAdapter.addAll(regions)
            }

            categoriesSpinner.adapter = regionsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}