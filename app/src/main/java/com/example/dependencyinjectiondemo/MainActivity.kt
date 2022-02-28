package com.example.dependencyinjectiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject  // field injection
    lateinit var smartPhone: SmartPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 모듈이 state를 갖게 된다면 create를 사용할 수 없다.
//        DaggerSmartPhoneComponent.create()
//            .inject(this@MainActivity)
//        smartPhone.makeACallWithRecording()

        // when instance requires variable inside a module
        // builder 함수로 생성, -> we have to add each module with a state here.
        DaggerSmartPhoneComponent.builder()
            .memoryCardModule(MemoryCardModule(1000))
            .build()
            .inject(this@MainActivity)
    }
}