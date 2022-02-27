package com.example.dependencyinjectiondemo

import dagger.Binds
import dagger.Module
import dagger.Provides

@Module // Change class to abstract class
abstract class NCBatteryModule {

    @Binds
    abstract fun bindsNCBattery(nickelCadmiumBattery: NickelCadmiumBattery) : Battery
}