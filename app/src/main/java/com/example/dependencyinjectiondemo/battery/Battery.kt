package com.example.dependencyinjectiondemo.battery

import android.util.Log
import javax.inject.Inject

// Change Battery class to interface
interface Battery {

    fun getPower()
}