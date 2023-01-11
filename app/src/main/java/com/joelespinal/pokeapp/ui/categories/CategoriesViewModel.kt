package com.joelespinal.pokeapp.ui.categories

import androidx.lifecycle.ViewModel
import com.joelespinal.pokeapp.repositories.UserRepository

class CategoriesViewModel : ViewModel() {
    private val userRepository = UserRepository.getInstance()


}