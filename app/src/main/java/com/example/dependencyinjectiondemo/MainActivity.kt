package com.example.dependencyinjectiondemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dependencyinjectiondemo.smartphone.SmartPhone
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject  // field injection
    lateinit var smartPhone: SmartPhone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // https://guides.codepath.com/android/Understanding-the-Android-Application-Class
        // Application class is the base class of an Android App.
        // All the content of this class is predefined by the Android FrameWork
        // Even though we cannot change the application class, we can give additional instructions to it by extending it.
        // ** when do we create a subclass of the application class?
        // If there are tasks that need to run before the creation of first activity.
        // If there are immutable data or global objects that needs to be shared across all components.
        (application as SmartPhoneApplication).smartPhoneComponent // android:name=".SmartPhoneApplication" Manifest에 등록함으로 앱 구성요소보다 먼저 인스턴스화 되게 함 https://developer.android.com/guide/topics/manifest/application-element#nm
            .inject(this) // Dagger constructs a new smartphone object and inject it to the Activity
        // makeACallWithRecording
        smartPhone.makeACallWithRecording()

        // 모듈이 state를 갖게 된다면 create를 사용할 수 없다.
//        DaggerSmartPhoneComponent.create()
//            .inject(this@MainActivity)
//        smartPhone.makeACallWithRecording()

        // when instance requires variable inside a module
        // builder 함수로 생성, -> we have to add each module with a state here.
//        DaggerSmartPhoneComponent.builder()
//            .memoryCardModule(MemoryCardModule(1000))
//            .build()
//            .inject(this@MainActivity)
    }
}
