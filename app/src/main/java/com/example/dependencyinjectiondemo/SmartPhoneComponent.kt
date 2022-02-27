package com.example.dependencyinjectiondemo

import dagger.Component

@Component
interface SmartPhoneComponent {
    // dependency type return
    fun getSmartPhone() : SmartPhone
}