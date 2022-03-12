package com.example.dependencyinjectiondemo.battery.nickelcadmium

import android.util.Log
import com.example.dependencyinjectiondemo.battery.Battery
import javax.inject.Inject

class NickelCadmiumBattery @Inject constructor() : Battery {
    override fun getPower() {
        Log.i("MYTAG", "Power from NickelCadmiumBattery")
    }
}