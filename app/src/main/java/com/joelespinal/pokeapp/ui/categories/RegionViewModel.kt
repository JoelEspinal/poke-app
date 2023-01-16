package com.joelespinal.pokeapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelespinal.pokeapp.repositories.RegionRepository
import kotlinx.coroutines.launch
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.Region

class CategoriesViewModel : ViewModel() {
    private val regionRepository = RegionRepository(PokeApiClient())

    private val _regions = MutableLiveData<List<Region>>()
    val regions: LiveData<List<Region>> = _regions

    fun getRegionNames() {
        viewModelScope.launch {
            _regions.postValue(regionRepository.getRegions())
        }
    }

}