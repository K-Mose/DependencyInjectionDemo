package com.example.dependencyinjectiondemo.di

import com.example.dependencyinjectiondemo.battery.Battery
import com.example.dependencyinjectiondemo.battery.nickelcadmium.NickelCadmiumBattery
import dagger.Binds
import dagger.Module

@Module // Change class to abstract class
abstract class NCBatteryModule {

    @Binds
    abstract fun bindsNCBattery(nickelCadmiumBattery: NickelCadmiumBattery) : Battery
}