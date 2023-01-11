package com.joelespinal.pokeapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.joelespinal.pokeapp.models.Team
import com.joelespinal.pokeapp.models.User

class UserRepository private constructor() {

    var currentUser = User(null, null)
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val database = Firebase.database
    private val users = database.getReference("users")

    companion object {
        private var instance: UserRepository? = null
        fun getInstance(): UserRepository? {
            if(instance == null) {
                instance = UserRepository()
            }

            return instance
        }
    }

    fun saveUid(uid: String) {
        val verifiedId = verify(uid)
      if (verifiedId == null) {
          users.setValue(uid)
      } else {
         if (verifiedId == uid) {
             var team = Team(uid, null, null)
             val teams = mutableMapOf<String, Team>()
             teams[uid] = team
             users.updateChildren(teams as Map<String, Any>)
         }
      }
    }

    fun verify(uid: String): String? {
        return users.child(uid).key
    }
}