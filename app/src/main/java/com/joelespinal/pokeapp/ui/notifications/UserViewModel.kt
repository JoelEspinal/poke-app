package com.joelespinal.pokeapp.ui.notifications

import androidx.lifecycle.ViewModel
import com.joelespinal.pokeapp.repositories.UserRepository

class UserViewModel : ViewModel() {
    private val userRepository = UserRepository.getInstance()


}