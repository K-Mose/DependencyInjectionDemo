package com.example.dependencyinjectiondemo.di

import com.example.dependencyinjectiondemo.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
// link module
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {

    // injector function
    fun inject(mainActivity: MainActivity)
}