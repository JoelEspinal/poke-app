package com.joelespinal.pokeapp.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.sargunvohra.lib.pokekotlin.client.PokeApiClient
import me.sargunvohra.lib.pokekotlin.model.NamedApiResource
import me.sargunvohra.lib.pokekotlin.model.Region

class RegionRepository(private val pokeClient: PokeApiClient) {

    private fun getRegionsNamedList(): List<NamedApiResource>{
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
}