package com.joelespinal.pokeapp.models


class Team(private var id: String?, val regionId: Int?, var pokemons: List<Int>?) {
    fun Team() {}

    fun getId(): String?{
        return id
    }


    @JvmName("getRegionId1")
    fun getRegionId(): Int? {
        return regionId
    }

    @JvmName("getPokemons1")
    fun getPokemons(): List<Int>? {
        return pokemons
    }
}
