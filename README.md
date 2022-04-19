# DependencyInjectionDemo
Dependency Injection With Dagger2 (https://www.udemy.com/course/android-architecture-componentsmvvm-with-dagger-retrofit/)

## Dependency - 의존성
Dependency - 의존성이란 한 클래스를 생성하기 위해 생성자에 다른 클래스가 들어가서 그 클래스가 꼭 필요한 것을 말합니다.
```kotlin
class A 
class B(a: A) 

fun main() {
  val a = A()
  val b = B(a)
}
```
위 클래스 B는 객체를 생성할 때 클래스 A가 반드시 필요하다 이것을 클래스 B가 클래스 A를 의존한다 말합니다. 

이런 의존성을 갖는 객체들을 생성할 의존성을 갖는 객체의 생성자내에서 의존하게 되는 객체를 생성하여 코드의 독립성을 떨어뜨리고 코드간 깊은 연관성을 갖게하여 테스트하기 어렵고 유지보수와 확장성이 떨어지게 됩니다. 
그렇기 때문에 **Dependency Inject - 의존성 주입**이 필요합니다.

## <a href="https://en.wikipedia.org/wiki/Dependency_injection">Dependency Inejection</a>
<a href="https://en.wikipedia.org/wiki/Dependency_injection">의존성 주입</a>이란 디자인 패턴 중 하나로, 의존성을 갖는 객체 내에서 의존하는 객처를 직접 생성하는 것이 아니라 *관심사를 분리함으로* 덜 복잡하고 연관성이 떨어지는 독립적인 코드를 생성할 수 있게 합니다. 
이러한 패턴은 서비스를 받는 객체가 어떻게 서비스를 생성하는지 알 필요가 없고, 서비스를 외부에있는 코드에 의해 제공받게 됩니다. 

의존성 주입을 사용하면 아래와 같은 이점이 있습니다. 
* 코드의 재사용성
* 쉬운 리팩터링
* 쉬운 테스트 

<figure>
<a href="https://www.youtube.com/watch?v=oK_XtfXPkqw">
<img src="https://user-images.githubusercontent.com/55622345/163950213-e18e224c-fc18-4d6f-9ec7-936fc80c4c98.png" max-width="1000px"></a>
<figcaption align = "center"><b>DAGGER 2 - A New Type of dependency injection</b></figcaption>
</figure>
위 사진을 보면 의존성 주입의 여부에 따른 코드 독립성을 보여줍니다. 의존성 주입이 없다면 각각 코드가 의존하는 코드를 직접적으로 필요하게 되므로 독립적으로 존재할 수 없게 되지만, 의존성 주입을 사용함에 따라 코드가 독립성을 가지며 의존하는 객체를 직접적으로 필요하지 않고 그 객체에 대한 구현체만 있다면 되므로 Fake나 Mock을 생성하여 테스트하기 쉬워집니다. 

## Scenario
![image](https://user-images.githubusercontent.com/55622345/158050431-a32ca0e1-8cb4-44cd-b1ed-8dbe2498b468.png)
<details>
  <summary>Full-Code</summary>
  
```kotlin
class Battery {
    init {
        Log.i("MYTAG","Battery Constructed")
    }

    fun getPower(){
        Log.i("MYTAG","Battery power is available")
    }
}

class MemoryCard {
    init {
        Log.i("MYTAG","Memory Card Constructed")
    }

    fun getSpaceAvailablity(){
        Log.i("MYTAG","Memory space available")
    }
}

class ServiceProvider {
    init {
        Log.i("MYTAG","Service Provider Constructed")
    }

    fun getServiceProvider(){
        Log.i("MYTAG","Service provider connected")
    }
}

class SIMCard(private  val serviceProvider: ServiceProvider) {
    init {
        Log.i("MYTAG","SIM Card Constructed")
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}

class SmartPhone(val battery: Battery, val simCard: SIMCard, val memoryCard: MemoryCard) {

    init {
        battery.getPower()
        simCard.getConnection()
        memoryCard.getSpaceAvailablity()
        Log.i("MYTAG", "SmartPhone Constructed")
    }

    fun makeACallWithRecording() {
        Log.i("MYTAG", "Calling.....")
    }
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val smartPhone = SmartPhone(
            Battery(),
            SIMCard(ServiceProvider()),
            MemoryCard()
        )
            .makeACallWithRecording()
    }
}
```
</details>
SmartPhoen 객체는 Battery, MemoryCard, SIMCard에 의존하고
SIMCard는 ServiceProvider에 의존합니다. 
SmartPhone 객체 생성 시 필요한 의존성을 주입합니다. 

## Injection Process 
<details>
  <summary>SmartPhone Dependeny Injection Progress</summary>
<br>1. SmartPhoneApplication - smartPhoneComponent = initDagger()
<br>  android:name에 등록된 Application부터 실행, initDagger 함수 실행 
<br>2, DaggerSmartPhoneComponent.builder().memoryCardModule(MemoryCardModule(1000))
<br>  DaggerSmartPhoneComponent 인스턴스 생성(builder pattern), 
<br>3. DaggerSmartPhoneComponent - memoryCardModule : this.memoryCardModule = Preconditions.checkNotNull(memoryCardModule);
<br>  memoryCardModule 주입, memoryCardModule = null
<br>4. DaggerSmartPhoneComponent.builder().memoryCardModule().build()
<br>  SmartPhoneComponent build()
<br>5. DaggerSmartPhoneComponent - build() : new DaggerSmartPhoneComponet
<br>  Checks that the component builder field {@code requirement} has been initialized, 
<br>  new DaggerSmartPhoneComponent(memoryCardModule) 반환
<br>6. DaggerSmartPhoneComponent - DaggerSmartPhoneComponent : initialize(memoryCardModuleParam)
<br>  DaggerSmartPhoneComponent initialize
<br>7. DaggerSmartPhoneComponent - initialize() - this.sIMCardProvider = SIMCard_Factory.create(ServiceProvider_Factory.create());
<br>  ServiceProvider_Factory.create() 실행
<br>8. ServiceProvider_Factory - InstanceHolder - private static final ServiceProvider_Factory INSTANCE = new ServiceProvider_Factory();
<br>  SIMCard_Factory create, Factory Pattern으로 IINSTANCE(ServiceProvider_Factory) 생성자 반환 
<br>9. SIMCard_Factory - create :  return new SIMCard_Factory(serviceProvider);
<br>10. SIMCard_Factory - SIMCard_Factory : this.serviceProvider = serviceProvider; 
<br>  반환받은 serviceProvider 생성자를 argument로 SIMCard_Factory 생성 후 반환 
<br>11. DaggerSmartPhoneComponent - initialize() - this.providersMemoryCardProvider = MemoryCardModule_ProvidersMemoryCardFactory.create(memoryCardModuleParam);
<br>  3번에서 생성된 memoryCardModule[memorySize = 1000]로 MemoryCardModule_ProvidersMemoryCardFactory.create 호출
<br>12. DaggerSmartPhoneComponent - return new MemoryCardModule_ProvidersMemoryCardFactory(module);
<br>  MemoryCardModule_ProvidersMemoryCardFactory 생성자 실행
<br>13. MemoryCardModule_ProvidersMemoryCardFactory - MemoryCardModule_ProvidersMemoryCardFactory : this.module = module; 
<br>  module 전역변수 설정, DaggerSmartPhoneComponent의 providersMemoryCardProvider에 MemoryCardModule_ProvidersMemoryCardFactory 주입, 12번 반환
<br>14.  DaggerSmartPhoneComponent - initialize() - this.smartPhoneProvider = DoubleCheck.provider(SmartPhone_Factory.create(((Provider) NickelCadmiumBattery_Factory.create()), sIMCardProvider, providersMemoryCardProvider));
<br>  smartPhoneProvider 생성을 위해 battery 생성, battery는 NickelCadmiumBattery_Factory.create 호출
<br>15. NickelCadmiumBattery_Factory - create() : return InstanceHolder.INSTANCE;
<br>  NickelCadmiumBattery_Factory 생성자 반환
<br>16. DaggerSmartPhoneComponent - initialize() - this.smartPhoneProvider = DoubleCheck.provider(SmartPhone_Factory.create(...)
<br>  15에서 생성된 Battery, 10에서 생성된 SIMCard, 13에서 생성된 MemoryCard로 SmartPhone Factory 실행
<br>17. SmartPhone_Factory - create : return new SmartPhone_Factory(batteryProvider, simCardProvider, memoryCardProvider);
<br>  SmartPhone_Factory 클래스 내부에서 SmartPhone_Factory 객체 생성
<br>18. SmartPhone_Factory - SmartPhone_Factory : 
<br>    this.batteryProvider = batteryProvider;    
<br>    this.simCardProvider = simCardProvider;
<br>    this.memoryCardProvider = memoryCardProvider;
<br>  각 객체 전역변수에 할당 후 17번에서 객체 리턴
<br>19. MainActivity - onCreate : (application as SmartPhoneApplication).smartPhoneComponent.inject(this)
<br>  application을 SmartPhoneApplication으로 할당 후 종속성 주입 
<br>  [inject() from interface SmartPhoneComponent]
<br>20. DaggerSmartPhoneComponent - inject : injectMainActivity(mainActivity);
<br>  내부적으로 생성된 injectMainActivty 함수 실행
<br>21. DaggerSmartPhoneComponent - injectMainActivity : MainActivity_MembersInjector.injectSmartPhone(instance, smartPhoneProvider.get());
<br>  Field injection으로 생성된 MainActivity_MembersInjector에서 Provider로 smartPhone MainActivity에 인젝션 주입 함수 실행 
<br>  smartPhoneProvider.get()부터 실행됨
<br>22. SmartPhone - get : return newInstance(batteryProvider.get(), simCardProvider.get(), memoryCardProvider.get());
<br>  18번에서 생성된 전역변수로 새 SmartPhone 인스턴스 생성
<br>  batteryProvider.get(), 
<br>  simCardProvider.get(), 
<br>  memoryCardProvider.get() 순차적으로 실행
<br>23. NickelCadmiumBattery_Factory - get : return newInstance(); 
<br>  22번의 batteryProvider.get() 실행, NickelCadmiumBattery_Factory의  newInstance 함수 실행
<br>24. NickelCadmiumBattery_Factory - newInstance : return new NickelCadmiumBattery();
<br>  새 NickelCadmiumBattery 인스턴스 반환 
<br>25. SIMCard_Factory - get : return newInstance(serviceProvider.get());
<br>  22번의 simCardProvider.get() 실행,  newInstance 함수 실행
<br>26. ServiceProvider_Factory - get : return newInstance();
<br>  SIMCard는 ServiceProvider에 종속성을 가지므로 ServiceProvider의 newInstance 함수 실행
<br>27. ServiceProvider_Factory - newInstance : return new ServiceProvider();
<br>  ServiceProvider 생성자 실행 
<br>28. ServiceProvider - init : Log.i("MYTAG","Service Provider Constructed")
<br>  ServiceProvider 생성자 로그 출력 
<br>29. SIMCard_Factory - newInstance : return new SIMCard(serviceProvider);
<br>  SIMCard 생성자 실행
<br>30. SIMCard - init : Log.i("MYTAG","SIM Card Constructed")
<br>  SIMCard 생성자 로그 출력
<br>31. MemoryCardModule_ProvidersMemoryCardFactory - get : return providersMemoryCard(module);
<br>  22번의 memoryCardProvider.get() 실행, providersMemoryCard 함수 실행, module은 초기 SmartPhoneApplication이 실행 될 때 Module에서 정의된 값
<br>32. MemoryCardModule_ProvidersMemoryCardFactory - providersMemoryCard : return Preconditions.checkNotNullFromProvides(instance.providersMemoryCard());
<br>  MemoryCardModule의 providersMemoryCard실행, 
<br>33. MemoryCardModule - providersMemoryCard : Log.i("MYTAG", "Size of the memory is $memorySize"); return MemoryCard()
<br>  로그 출력 후 MemoryCard 생성자 실행 
<br>34. MemoryCard - init : Log.i("MYTAG","Memory Card Constructed")
<br>  MemoryCard 생성자 로그 출력, 33번에서 생성자 리턴.
<br>35. SmartPhone - newInstance : return new SmartPhone(battery, simCard, memoryCard);
<br>  22번의 newInstance에서 호출됨, SmartPhone 생성자 실행
<br>36. SmartPhone - init : 
<br>    Log.i("MYTAG", "SmartPhone Constructed")
<br>    battery.getPower()
<br>    simCard.getConnection()
<br>    memoryCard.getSpaceAvailablity()
<br>  SmartPhone 생성자 로그 출력. battery, simCard, memoryCard 각각의 함수 실행
<br>37. MainActivity_MembersInjector - injectSmartPhone : instance.smartPhone = smartPhone;
<br>  21번의 injectSmartPhone 함수 실행, MainActivity의 전역변수 smartPhone에 생성된 smartPhone 주입
<br>38. DaggerSmartPhoneComponent - injectMainActivity : return instance;
<br>  주입된 instance 반환
<br>39 MainActivity - onCreate : smartPhone.makeACallWithRecording()
<br>  makeACallWithRecording 함수 실행, 
<br>40. SmartPhone - makeACallWithRecording : Log.i("MYTAG", "Calling.....")
<br>  함수 로그 출력(onCreate에서 smartPhone 객체가 생성됨을 알림)
</details>  
## Ref. 
https://developer.android.com/training/dependency-injection
https://developer.android.com/training/dependency-injection/dagger-basics  <br>
https://www.youtube.com/watch?v=oK_XtfXPkqw  <br>
https://docs.google.com/presentation/d/1fby5VeGU9CN8zjw4lAb2QPPsKRxx6mSwCe9q7ECNSJQ/pub?start=false&loop=false&delayms=3000&slide=id.g36bd8951a_05  <br>
