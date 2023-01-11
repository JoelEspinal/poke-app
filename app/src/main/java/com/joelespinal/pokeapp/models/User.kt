package com.joelespinal.pokeapp.models

class User(var uid: String?, val teams: List<Team>?){

    @JvmName("getUid1")
    fun getUid(): String? {
        return uid
    }

    @JvmName("getTeams1")
    fun getTeams(): List<Team>? {
        return teams
    }
}
