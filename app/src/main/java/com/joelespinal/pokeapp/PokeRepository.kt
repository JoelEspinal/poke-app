package com.joelespinal.pokeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.*

class PokeRepository(private val pokeClient: PokeApiClient) {

    private var selectedRegion = 0
    private var pokemonsList = mutableListOf<Pokemon>()
    private val _pokemons = MutableLiveData<List<Pokemon>>()
    val pokemons: LiveData<List<Pokemon>> = _pokemons

    private val _entriesLength = MutableLiveData<Int>()
    val entriesLength : LiveData<Int> = _entriesLength

    fun getRegionsNamedList(): List<NamedApiResource>{
        val regionNamedList = pokeClient.getRegionList(0, 20)
        return regionNamedList.results
    }

    suspend fun getRegions(): List<Region> {
        return withContext(Dispatchers.IO) {
            val regions = mutableListOf<Region>()
            for(named in getRegionsNamedList()) {
                val region = pokeClient.getRegion(named.id)
                regions.add(region)
            }

            return@withContext regions
        }
    }

    suspend fun pokemonsByRegion(regionId: Int, fromIndex: Int) {
        if (selectedRegion != regionId && selectedRegion > 0) {
            pokemonsList = mutableListOf()
            _pokemons.postValue(pokemonsList)
        }

        selectedRegion = regionId

        return withContext(Dispatchers.IO) {
            val region = pokeClient.getRegion(selectedRegion)
            for (pokedexResource in region.pokedexes) {
                val pokedex = pokeClient.getPokedex(pokedexResource.id)
                val pokemonEntries = pokedex.pokemonEntries
                _entriesLength.postValue(pokemonEntries.size - 1)

                val entries = subEntries(pokemonEntries, fromIndex, (fromIndex + 15))
                for (entry in entries) {
                    val specie = pokeClient.getPokemonSpecies(entry.pokemonSpecies.id)
                    for (variety in specie.varieties) {
                        val pokemon = pokeClient.getPokemon(variety.pokemon.id)
                        pokemonsList.add(pokemon)
                    }
                }
            }
            _pokemons.postValue(pokemonsList)
        }
    }

    suspend fun subEntries(entries: List<PokemonEntry>, fromIndex: Int, toIndex: Int):  List<PokemonEntry> {
        val length = entries.size - 1
        val workerList: List<PokemonEntry>
        val slice = toIndex - fromIndex

        workerList = if ((fromIndex + slice) <= length) {
            entries.subList(fromIndex, (fromIndex + slice))
        } else {
            entries.subList(fromIndex, length)
        }

        return workerList
    }
}
