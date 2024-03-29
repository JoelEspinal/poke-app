package com.joelespinal.pokeapp.ui.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.adapters.PokemonAdapter
import com.joelespinal.pokeapp.databinding.FragmentPokemonBinding
import me.sargunvohra.lib.pokekotlin.model.Region

class PokemonFragment : Fragment(), OnItemSelectedListener {

    private var selectedRegion: Region? = null
    private var _binding: FragmentPokemonBinding? = null
    private lateinit var homeViewModel: PokemonViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var categoriesSpinner: Spinner

    private lateinit var regionsLiveData: LiveData<List<Region>>
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var selectedPokemonsCount: TextView
    private lateinit var placeholder: FrameLayout
    private lateinit var createTeamButton: FloatingActionButton
    private lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(PokemonViewModel::class.java)

        _binding = FragmentPokemonBinding.inflate(inflater, container, false)
        val root: View = binding.root

        regionsLiveData = homeViewModel.regions
        homeViewModel.getRegionNames()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesSpinner = view.findViewById(R.id.categories_spinner)
        swipeContainer = view.findViewById(R.id.container)
        selectedPokemonsCount = view.findViewById(R.id.selected_pokemons_count)
        createTeamButton = view.findViewById(R.id.create_team_button)
        placeholder = view.findViewById(R.id.placeholder)

        initAdapter()
        initRecycleView(view)
        initButtons()
    }

    private fun initButtons() {
        homeViewModel.selectedPokemoms.observe(viewLifecycleOwner) {
            var size = homeViewModel.selectedPokemoms.value?.size
            selectedPokemonsCount.text = "$size"

            if (size != null) {
                createTeamButton.isEnabled = (((size > 2) && (size < 7)))
            }
        }
    }

    private fun initAdapter() {
        context?.let { context ->
            var regionsAdapter =
                ArrayAdapter(context, android.R.layout.simple_spinner_item, mutableListOf<String>())
            regionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            regionsLiveData.observe(viewLifecycleOwner) { regions ->
                regionsAdapter.addAll(regions.map { it.name })
                categoriesSpinner.isEnabled = regions.isNotEmpty()
                categoriesSpinner.adapter = regionsAdapter
                categoriesSpinner.onItemSelectedListener = this
            }
        }
    }


    var fromIndex = 0
    private fun initRecycleView(view: View) {
        context?.let { context ->
            val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view_categories)
            pokemonAdapter = PokemonAdapter(context, homeViewModel)
            recyclerView.adapter = pokemonAdapter
            recyclerView.layoutManager = GridLayoutManager(activity, 2)

            activity?.let { activity ->
                homeViewModel.pokemons.observe(activity) { pokemons ->
                    pokemonAdapter.submitList(pokemons)
                    if (pokemons.isEmpty()) {
                        placeholder.visibility = View.VISIBLE
                    } else {
                        placeholder.visibility = View.INVISIBLE
                    }
                }
            }

               swipeContainer.setOnRefreshListener {
                   selectedRegion?.let {
                       fromIndex += 10
                       homeViewModel.getPokemonsByRegion(it.id, fromIndex)
                       activity?.let { activity ->
                           homeViewModel.pokemons.observe(activity) { pokemons ->
                               pokemonAdapter.submitList(pokemons)
                               pokemonAdapter.notifyDataSetChanged()
                           }
                       }

                   }

                   swipeContainer.isRefreshing = false
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position > 0) {
            var index = position - 1
            if(index >= regionsLiveData.value?.size!!) {
                index = regionsLiveData.value?.size!! - 1
            }

            selectedRegion = regionsLiveData.value?.get(index)
            selectedRegion?.let {
                selectedRegion?.id?.let { homeViewModel.getPokemonsByRegion(it, 0) }
            }
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
