package com.example.dependencyinjectiondemo

import dagger.Module
import dagger.Provides

@Module
class MemoryCardModule {
    // It's not recommended for a module to have a state.
    // That means a module should not have instance variables.
    // Because, that can lead to an unpredictable behavior.
    // You should create modules only when you actually need it.

    @Provides
    fun providersMemoryCard(): MemoryCard {
        return MemoryCard()
    }
}