package com.joelespinal.pokeapp.ui.home

import android.net.ipsec.ike.ChildSessionCallback
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnChildScrollUpCallback
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.adapters.PokemonAdapter
import com.joelespinal.pokeapp.databinding.FragmentHomeBinding
import me.sargunvohra.lib.pokekotlin.model.Region

class HomeFragment : Fragment(), OnItemSelectedListener {

    private lateinit var selectedRegion: Region
    private var _binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var categoriesSpinner: Spinner

    private lateinit var regionsLiveData: LiveData<List<Region>>
    private lateinit var swipeContainer: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        regionsLiveData = homeViewModel.regions
        homeViewModel.getRegionNames()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesSpinner = view.findViewById(R.id.categories_spinner)
        swipeContainer = view.findViewById(R.id.container)

        initAdapter()
        initRecycleView(view)
    }

    private fun initAdapter() {
        context?.let { context ->
            var regionsAdapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_item, mutableListOf<String>())
            regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            regionsLiveData.observe(viewLifecycleOwner) { regions ->
                regionsAdapter.addAll(regions.map { it.name })
            }

            categoriesSpinner.adapter = regionsAdapter
            categoriesSpinner.onItemSelectedListener = this


        }
    }

    private fun initRecycleView(view: View) {
        context?.let { context ->
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_pokemons)
            val adapter = PokemonAdapter(context, homeViewModel)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(activity, 2)

            activity?.let { activity ->
                homeViewModel.pokemons.observe(activity) { pokemons ->
                    adapter.submitList(pokemons)
                }
            }

            var fromIndex = 0
               swipeContainer.setOnRefreshListener () {
                swipeContainer.isRefreshing = false
                fromIndex += 20
                homeViewModel.getPokemonsByRegion(selectedRegion.id, fromIndex)
            }

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position > 0) {
            var index = position - 1
            if(index >= regionsLiveData.value?.size!!) {
                index = regionsLiveData.value?.size!! - 1
            }

            selectedRegion = regionsLiveData.value?.get(index)!!
            selectedRegion?.id?.let { homeViewModel.getPokemonsByRegion(it, 0) }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
