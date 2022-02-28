package com.example.dependencyinjectiondemo

import android.util.Log
import dagger.Module
import dagger.Provides

@Module // 모듈에 변수 추가
class MemoryCardModule(val memorySize: Int) {
    // It's not recommended for a module to have a state.
    // That means a module should not have instance variables.
    // Because, that can lead to an unpredictable behavior.
    // You should create modules only when you actually need it.

    @Provides
    fun providersMemoryCard(): MemoryCard {
        Log.i("MYTAG", "Size of the memory is $memorySize")
        return MemoryCard()
    }
}