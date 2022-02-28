package com.example.dependencyinjectiondemo

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

// 새 fragment가 생성될 때 Dagger가 inject된 새로운 인스턴스 생성, cf. 화면 회전
@Singleton // Singleton으로 설정, 인스턴스를 1개만 사용하여 전체 앱에 공유 - reuse the existing singleton instance
class SmartPhone @Inject constructor(val battery: Battery, val simCard: SIMCard, val memoryCard: MemoryCard) {

    init {
        Log.i("MYTAG", "SmartPhone Constructed")
        battery.getPower()
        simCard.getConnection()
        memoryCard.getSpaceAvailablity()
    }

    fun makeACallWithRecording() {
        Log.i("MYTAG", "Calling.....")
    }
}