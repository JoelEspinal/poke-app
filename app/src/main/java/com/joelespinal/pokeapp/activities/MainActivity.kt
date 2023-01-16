package com.joelespinal.pokeapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joelespinal.pokeapp.LoginActivity
import com.joelespinal.pokeapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            launch {
                delay(2000L)
                val intent = Intent(applicationContext, LoginActivity::class.java);
                startActivity(intent)
            }
        }
    }
}