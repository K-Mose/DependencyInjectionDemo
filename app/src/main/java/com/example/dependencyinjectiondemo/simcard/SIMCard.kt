package com.example.dependencyinjectiondemo.simcard

import android.util.Log
import com.example.dependencyinjectiondemo.simcard.service.ServiceProvider
import javax.inject.Inject

class SIMCard @Inject constructor(private  val serviceProvider: ServiceProvider) {
    init {
        Log.i("MYTAG","SIM Card Constructed")
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}