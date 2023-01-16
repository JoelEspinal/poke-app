package com.joelespinal.pokeapp.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.joelespinal.pokeapp.R
import com.joelespinal.pokeapp.adapters.PokemonAdapter
import com.joelespinal.pokeapp.ui.pokemon.PokemonViewModel
import me.sargunvohra.lib.pokekotlin.model.Region

const val REGION_ID = "REGION_ID"

class PokemonActivity : AppCompatActivity() {

    private var regionId = 1
    private var selectedRegion: Region? = null
    private lateinit var pokemonViewModel: PokemonViewModel

    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var selectedPokemonsCount: TextView
    private lateinit var placeholder: FrameLayout
    private lateinit var createTeamButton: FloatingActionButton
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var context: PokemonActivity
    private lateinit var recyclerView: RecyclerView

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)
        context = PokemonActivity@this

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        regionId = intent.getIntExtra(REGION_ID, 1)
        pokemonViewModel = ViewModelProvider(context).get(PokemonViewModel::class.java)

        recyclerView = findViewById(R.id.recycler_view_categories)
        selectedPokemonsCount = findViewById(R.id.selected_pokemons_count)
        createTeamButton = findViewById(R.id.create_team_button)
        placeholder = findViewById(R.id.placeholder)
        swipeContainer = findViewById(R.id.container)
        progressBar = findViewById(R.id.progress_bar)

        initRecycleView()
        initButtons()
    }

    override fun onStart() {
        super.onStart()
        pokemonViewModel.getPokemonsByRegion(regionId, 0)
    }

    private fun initButtons() {
        pokemonViewModel.selectedPokemoms.observe(context) {
            val size = pokemonViewModel.selectedPokemoms.value?.size
            selectedPokemonsCount.text = "$size"

            size?.let {
                createTeamButton.isEnabled =  size in 3..6
            }
        }
    }

    var fromIndex = 0
    private fun initRecycleView() {
            pokemonAdapter = PokemonAdapter(context, pokemonViewModel)
            recyclerView.adapter = pokemonAdapter
            recyclerView.layoutManager = GridLayoutManager(context, 2)

            pokemonViewModel.pokemons.observe(context) { pokemons ->
            pokemonAdapter.submitList(pokemons)
            progressBar.visibility = if (swipeContainer.isRefreshing) View.VISIBLE else View.INVISIBLE
            placeholder.visibility = if (pokemons.isEmpty()) View.VISIBLE else View.INVISIBLE

            swipeContainer.setOnRefreshListener {
                selectedRegion?.let {
                    fromIndex += 10
                    pokemonViewModel.getPokemonsByRegion(it.id, fromIndex)
                    pokemonViewModel.pokemons.observe(context) { pokemons ->
                        pokemonAdapter.submitList(pokemons)
                        pokemonAdapter.notifyDataSetChanged()
                    }
                }

                swipeContainer.isRefreshing = false
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}