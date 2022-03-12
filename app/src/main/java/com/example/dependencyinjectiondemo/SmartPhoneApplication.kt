package com.example.dependencyinjectiondemo

import android.app.Application
import com.example.dependencyinjectiondemo.di.DaggerSmartPhoneComponent
import com.example.dependencyinjectiondemo.di.SmartPhoneComponent
import com.example.dependencyinjectiondemo.di.MemoryCardModule

// subclass of MainClass
class SmartPhoneApplication : Application() {
    lateinit var smartPhoneComponent: SmartPhoneComponent
    override fun onCreate() {
        smartPhoneComponent = initDagger()
        super.onCreate()
    }

    private fun initDagger(): SmartPhoneComponent =
        DaggerSmartPhoneComponent.builder()
            .memoryCardModule(MemoryCardModule(1000))
            .build()
}