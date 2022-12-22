package com.joelespinal.pokeapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient

class PokeRepository(private val pokeClient: PokeApiClient) {

    suspend fun getRegions(): List<String> {
        return withContext(Dispatchers.IO) {
            val regionList = pokeClient.getRegionList(0, 20)
            val regionsName = regionList.results.map { it.name }
            return@withContext regionsName
        }
    }
}