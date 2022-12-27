package com.joelespinal.pokeapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelespinal.pokeapp.PokeRepository
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

    val _pokemonsLength = MutableLiveData<Int>()
    val pokemonsLength = _pokemonsLength

    fun getRegionNames() {
        viewModelScope.launch {
            _regions.postValue(pokeRepository.getRegions())
        }
    }

    fun getPokemonsByRegion(regionId: Int, fromIndex: Int) {
        viewModelScope.launch {
            pokeRepository.pokemonsByRegion(regionId, fromIndex)
            _pokemonsLength.postValue(pokeRepository.entriesLength.value)
            _pokemons.postValue(pokeRepository.pokemons.value)
        }
    }
}