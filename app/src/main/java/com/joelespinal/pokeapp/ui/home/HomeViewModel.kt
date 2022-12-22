package com.joelespinal.pokeapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelespinal.pokeapp.PokeRepository
import kotlinx.coroutines.launch
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class HomeViewModel : ViewModel() {

    private val pokeRepository = PokeRepository(PokeApiClient())
    private val _regionsName = MutableLiveData<List<String>>()

    val regionsName: LiveData<List<String>> = _regionsName

    fun getRegionNames() {
        viewModelScope.launch {
            _regionsName.postValue(pokeRepository.getRegions())
        }
    }
}