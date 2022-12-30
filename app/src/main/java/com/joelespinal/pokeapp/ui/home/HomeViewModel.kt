package com.joelespinal.pokeapp.ui.home

import android.widget.Toast
import androidx.lifecycle.*
import com.joelespinal.pokeapp.PokeRepository
import com.joelespinal.pokeapp.models.Team
import kotlinx.coroutines.launch
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Pokemon
import me.sargunvohra.lib.pokekotlin.model.Region

class HomeViewModel : ViewModel() {
    private val pokeRepository = PokeRepository(PokeApiClient())

    private val _regions = MutableLiveData<List<Region>>()
    val regions: LiveData<List<Region>> = _regions

    private val _pokemons = MutableLiveData<List<Pokemon>>()
    val pokemons: LiveData<List<Pokemon>> = _pokemons

     private val cachedPokemon = mutableListOf<Pokemon>()
    private val _selectedPokemoms = MutableLiveData<List<Pokemon>>(cachedPokemon)
    val selectedPokemoms : LiveData<List<Pokemon>> = _selectedPokemoms

    lateinit var selectedRegion: Region

//    var currentTeam = Team(0, null, mutableListOf<Pokemon>())

    fun getRegionNames() {
        viewModelScope.launch {
            _regions.postValue(pokeRepository.getRegions())
        }
    }

    fun getPokemonsByRegion(regionId: Int, fromIndex: Int) {
        viewModelScope.launch {
            pokeRepository.pokemonsByRegion(regionId, fromIndex)
            _pokemons.postValue(pokeRepository.pokemons.value)
        }
    }

    fun selectPokemon(pokemon : Pokemon) {
        val size = cachedPokemon.size
        if (size <= 6) {
            cachedPokemon.add(pokemon)
            _selectedPokemoms.postValue(cachedPokemon)
        }
    }

    fun deselectPokemon(pokemon : Pokemon) {
        cachedPokemon.removeIf { pokemon.id == it.id }
        _selectedPokemoms.postValue(cachedPokemon)
    }
}
