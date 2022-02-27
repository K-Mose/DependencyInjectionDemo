package com.example.dependencyinjectiondemo

import dagger.Component

// link module
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
interface SmartPhoneComponent {
    // dependency type return
    fun getSmartPhone() : SmartPhone
}