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

## Three Way of Dependency Injection

### Constructor Injection 
```kotlin
val smartPhone = SmartPhone(
    Battery(),
    SIMCard(ServiceProvider()),
    MemoryCard()
).makeACallWithRecording()
```
Constructor Injection은 생성자를 생성 할 때 필요한 객체를 넘겨주어 생성합니다. 

### Method Injection 
```kotlin 
class SIMCard(private  val serviceProvider: ServiceProvider) {
    private lateinit var serviceProvider: ServiceProvider 
    init {
        Log.i("MYTAG","SIM Card Constructed")
    }
    
    fun setServiceProvider(serviceProvider: ServiceProvider) {
        this.serviceProvider = serviceProvider
    }

    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}
```
Method Injection은 `simCard.setServiceProvider(ServiceProvider())` 처럼 외부에서 객체의 의존성을 주입하는 메소드에 인자 값을 넘겨주어 메소드를 실행시켜 의존성을 주입합니다. 

### Field Injection 
```kotlin 
class SIMCard(private  val serviceProvider: ServiceProvider) {
    public lateinit var serviceProvider: ServiceProvider 
    init {
        Log.i("MYTAG","SIM Card Constructed")
    }
    
    fun getConnection(){
        serviceProvider.getServiceProvider()
    }
}
```
Field Injection은 의존성이 시스템에 의해 초기화 되거나 생성자가 없는 객체, 즉 Constructor Injection를 사용할 수 없는 객체에서 사용 가능합니다. `simCard.serviceProvider = ServiceProvider()`처럼 해당 객체가 생성된 후 의존성을 주입하게 됩니다. 


## DI With Dagger
수동(Manual)의존성 주입은 코드의 독립성을 높이지만 대규모의 프로젝트에서 작성하기에 많은 시간이 들고 재사용성이 떨어지며 반복적인 패턴으로 비효율적인 생산성을 만들 수 있기 때문에 외부 라이브러리로 자동 생성하는 것이 효율적일 수 있습니다. 

Dagger는 자동으로 코드를 생성해주며 컴파일 시간에 코드가 생성되기때문에 코드를 추적하기 쉽고 다른 솔루션들보다 성능적으로도 뛰어납니다. 

### Constructor Injection With Dagger
#### @Inject
우선  객체의 주생성자에 @Inject 어노테이션을 사용해서 Dagger가 Consturctor를 만들 수 있게 허용합니다. 
```kotlin
class Battery constructor()

class MemoryCard constructor()

class ServiceProvider constructor()

class SIMCard constructor(private  val serviceProvider: ServiceProvider)

class SmartPhone @Inject constructor(val battery: Battery, val simCard: SIMCard, val memoryCard: MemoryCard)
```

#### <a href="https://developer.android.com/training/dependency-injection/dagger-basics#dagger-components">@Component</a>
Dagger는 의존성의 그래프를 그려서 dependency가 어디에 필요한지 찾아냅니다. 그러기 앞서 @Componet 어노테이션의 인터페이스가 필요합니다. 
@Component 인터페이스 안에는 dependency 클래스의 인스턴스를 반환하는 함수를 정의할 수 있습니다. 
```kotlin 
@Component
interface SmartPhoneComponent {
    fun getSmartPhone() : SmartPhone
}
```


컴포넌트를 생성했다면 Dagger가 코드를 생성할 수 있도록 `Build - Rebuild Project`로 프로젝트를 재생성 합니다. 
#### java(generated) 
<img src="https://user-images.githubusercontent.com/55622345/164005849-da0eb70f-18aa-4fed-a4e8-e83caf09a648.png" max-width="600px"/><br>

Dagger의 어노테이션을 사용해서 build를 하게되면 자동으로 java 코드가 생성됩니다. Dagger는 코드를 Factory Pattern으로 생성하며 아래와 같이 생성됩니다. 
```kotlin 
// Generated by Dagger (https://dagger.dev).
import …

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerSmartPhoneComponent implements SmartPhoneComponent {
  private final DaggerSmartPhoneComponent smartPhoneComponent = this;

  private DaggerSmartPhoneComponent() {


  }

  public static Builder builder() {
    return new Builder();
  }

  public static SmartPhoneComponent create() {
    return new Builder().build();
  }

  private SIMCard sIMCard() {
    return new SIMCard(new ServiceProvider());
  }

  @Override
  public SmartPhone getSmartPhone() {
    return new SmartPhone(new Battery(), sIMCard(), new MemoryCard());
  }

  public static final class Builder {
    private Builder() {
    }

    public SmartPhoneComponent build() {
      return new DaggerSmartPhoneComponent();
    }
  }
}
```
@Component 어노테이션으로 생성한 인터페이스를 구현한 클래스로 자동으로 각각의 @Inject 클래스가 작성된 것을 알 수 있습니다. 
@Component 어노테이션으로 생성한 인터페이스는 항상 Dagger나는 접두사 를 가지며, Override된 `getSmartPhone()`를 가지고 `MainActivity`에서 사용할 수 있습니다.

#### Using MainActivity
생성된 `DaggerSmartPhoneComponent`를 가지고 MainActivity에서 아래와 같이 객체를 생성할 수 있습니다. 
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerSmartPhoneComponent.create()
            .getSmartPhone()
            .makeACallWithRecording()
    }
}
```

@Inject 어노테이션은 사용할 모든 의존성 객체의 생성자에 표시하고, @Component 어노테이션을 Component Interface에 사용하면 Dagger가 모든 dependency을 생성할 수 있게 합니다. 

## Module 
많은 상황에서 우리는 외부 라이브러리 사용 등으로 실제 클래스를 열어서 생성자에 @Inject 어노테이션을 붙일 수 없는 상황이 오게 됩니다. 
Dagger.dev에서는 아래와 같은 상황에서는 @Inject 어노테이션을 사용할 수 없다고 합니다.
- Interfaces can’t be constructed.
- Third-party classes can’t be annotated.
- Configurable objects must be configured!

### Provides
실제로 소유하고 있지 않은 클래스에 대해서는 어떻게 의존성을 주입할 수 있을까요?  
Dagger의 Module과 Provides 함수로 이러한 의존성을 제공할 수 있습니다. 

`MemoryCard`클래스가 외부 라이브러리에서 불러와 소유하고 있지 않다고 가정해 봅시다. 그래서 생성자에 @Inject 어노테이션을 붙일 수 없는 상황이 됩니다. Dagger로 제공하기 위해서 새로운 `MemoryCardModule`클래스를 만듭니다. 그래고 제공할 의존성을 반환하는 provider함수를 작성합니다. 
```kotlin 
import dagger.Module
import dagger.Provides

@Module
class MemoryCardModule {
    @Provides
    fun providesMemoryCard(): MemoryCard {
        return MemoryCard()
    }
}
```
provider 함수에 @Provides 어노테이션을 추가해 Dagger가 알 수 있게 합니다. 

그리고 Component 인터페이스의 @Compoenet 어노테이션에 Module을 추가합니다. 
```kotlin 
@Component(modules = [MemoryCardModule::class])
interface SmartPhoneComponent {
    fun getSmartPhone(): SmartPhone
}
```

<details>
  <summary>DaggerSmartPhoneComponent - auto generated</summary>

```kotlin
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DaggerSmartPhoneComponent implements SmartPhoneComponent {
  private final MemoryCardModule memoryCardModule;

  private final DaggerSmartPhoneComponent smartPhoneComponent = this;

  private DaggerSmartPhoneComponent(MemoryCardModule memoryCardModuleParam) {
    this.memoryCardModule = memoryCardModuleParam;

  }

  public static Builder builder() {
    return new Builder();
  }

  public static SmartPhoneComponent create() {
    return new Builder().build();
  }

  private SIMCard sIMCard() {
    return new SIMCard(new ServiceProvider());
  }

  @Override
  public SmartPhone getSmartPhone() {
    return new SmartPhone(new Battery(), sIMCard(), MemoryCardModule_ProvidesMemoryCardFactory.providesMemoryCard(memoryCardModule));
  }

  public static final class Builder {
    private MemoryCardModule memoryCardModule;

    private Builder() {
    }

    public Builder memoryCardModule(MemoryCardModule memoryCardModule) {
      this.memoryCardModule = Preconditions.checkNotNull(memoryCardModule);
      return this;
    }

    public SmartPhoneComponent build() {
      if (memoryCardModule == null) {
        this.memoryCardModule = new MemoryCardModule();
      }
      return new DaggerSmartPhoneComponent(memoryCardModule);
    }
  }
}
```
위의 자동 생성된 클래스를 보면 MemoryCard 클래스가 Module로 생성되는 것을 볼 수 있습니다. 
</details>


모듈은 꼭 필요할 때만 추가하는 것이 좋습니다. 모든 클래스의 생성자에 @Inject 어노테이션을 추가할 수 있다면 @Module을 생성할 필요가 없습니다. 


## Working With Interface 
인터페이스가 클래스 대신 의존성으로 필요할 때가 있습니다. 
우선 예를 들기 위해 `Battery`클래스를 인터페이스로 변경하겠습니다. 
```kotlin
interface Battery {
    fun getPower()
}
```
인터페이스로 수정하면 `class SmartPhone @Inject constructor(val battery: Battery, val simCard: SIMCard, val memoryCard: MemoryCard)` 여기 필요한 battery 파라메터는 인터페이스로 바뀌기 때문에 `Battery`를 생성할 수 없어 오류가 생기게 됩니다. 

여기서는 `Battery`인터페이스를 구현하는 클래스가 필요하고, 그 클래스를 module로써 의존성을 제공해야 합니다. 우선 `Battery` 인터페이스를 구현하는 클래스를 작성합니다. 
```kotlin 
class NickelCadmiumBattery @Inject constructor() : Battery {
    override fun getPower() {
        Log.i("MYTAG", "Power from NickelCadmiumBattery")
    }
}
```
이 상태로 실행을 하면 클래스를 생성해 @Inject 어노테이션을 붙였음에도 에러가 발생합니다. 개발자가 봤을 때는 이 클래스가 `Battery`인터페이스를 구현하는 클래스 인것을 알 수 있지만 Dagger는 그렇지 못합니다. Dagger가 알기 위해서는 module을 생성하고 이 클래스를 의존성으로 제공해주어야 합니다. 

### Bind
우선 구현 클래스를 제공할 모듈을 생성합니다. 모듈을 abstract로 작성한다면 더욱 간단히 작성할 수 있습니다.
```kotlin
@Module
abstract class NCBatteryModule {
    @Binds
    abstract fun providesNCBattery(nickelCadmiumBattery: NickelCadmiumBattery): Battery
}
```
`NickelCadmiumBattery`클래스에서 이미 @Inject 어노테이션으로 생성자가 있으므로 모듈 클래스에는 body를 선언할 필요가 없으므로 abstract class로 생성합니다. 
`NickelCadmiumBattery`의 @Inject 어노테이션으로 Dagger에서 생성자가 생성되므로 `providesNCBattery(nickelCadmiumBattery: NickelCadmiumBattery)` 함수에서 생성자를 받고 리턴합니다. 

마지막으로 생성한 모듈을 Component에 추가시킵니다. <br>
``
@Component(modules = [MemoryCardModule::class, NCBatteryModule::class])
``

https://dagger.dev/dev-guide/ 에는 아래와 같이 @Bind를 사용하는 것이 선호됩니다. 
<p>
Note: Using @Binds is the preferred way to define an alias because Dagger only needs the module at compile time, and can avoid class loading the module at runtime.
</p>


## Field Injection 
`MainActivity`에서 `SmartPhoneComponent`인터페이스의 `getSmartPhone()` 함수를 호출함으로 의존성을 호출하였습니다. 이러한 방법은 여러 activity와 fragment가 존재하는 프로젝트 내에서는 좋은 방법이 아닙니다. 
만약 SmartPhone과 같은 여러개의 dependency가 필요하다면 각각의 Component를 만들고 getter 메소드를 작성하여 Activity마다 호출하게 될 것입니다. 

이러한 것들을 Field Injection으로 쉽게 변경할 수 있습니다. 

우선 기존 getter 함수를 Inject 함수로 변경합니다. 
```kotlin 
interface SmartPhoneComponent {
    fun inject(mainActivity: MainActivity)
}
```
함수의 파라메터에 사용할 Activity(or Fragment)를 넣어줌으로 해당 Activity(or Fragment)에서 사용할 것을 알려줍니다. 

위의 함수를 `MainActivity`에서 사용하기 위해 아래와 같이 변경합니다. 
```kotlin 
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var smartPhone: SmartPhone
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerSmartPhoneComponent.create()
            .inject(this)
        smartPhone.makeACallWithRecording()
    }
}
```
이제 `DaggerSmartPhoneComponent`클래스가 생성되면서 전역변수로 사용된 `samartPhone` 필드에 Field injection이 주입됩니다. 

*What is different?* - 기존의 `getSmartPhone()` 함수로 DI를 실행하게된다면 SmartPhone dependency만 사용 가능합니다. 다른 dependency를 사용하고 싶다면 다른 Component interface를 생성하여 실행시켜야 할 것입니다. <br>
Field injection을 사용함으로 하나의 Component로 의존성을 주입할 수 있게 됩니다. 

## State Of A Module
보통은 모듈애 상태 값을 넣는 것은 사용되지 않습니다. 하지만 시나리오에 따라 모듈안에 변수가 필요할 수 있습니다.<br>
아래와 같이 `MemoryCardModule`클래스에 초기화를 위한 메모리 크기 값을 전달한다고 가정해 봅시다.
```kotlin
@Module
class MemoryCardModule(val memorySize:Int) {
    @Provides
    fun providesMemoryCard(): MemoryCard {
        Log.i("MYTAG", "Size of the memory is $memorySize")
        return MemoryCard()
    }
}
```
프로젝트를 rebuild 하게 되면 `MainActivty`에서 더이상 `DaggerSmartPhoneComponent.create()`를 사용할 수 없게 됩니다. 
<img src="https://user-images.githubusercontent.com/55622345/164466708-5ea85c15-1bc1-4fef-8854-c22e6b1f3762.png" max-width="800px"/>

<details>
<summary>rebuild</summary>

`DaggerSmartPhoneComponent`클래스를 보면 기존의 `create()` 메소드는 사라지고 `build()`메소드가 변경된 것을 볼 수 있습니다. 
```kotlin
public static SmartPhoneComponent create() {
  return new Builder().build();
}
…
public SmartPhoneComponent build() {
  if (memoryCardModule == null) {
    this.memoryCardModule = new MemoryCardModule();
  }
  return new DaggerSmartPhoneComponent(memoryCardModule);
}
```
↓
```kotlin
public SmartPhoneComponent build() {
  Preconditions.checkBuilderRequirement(memoryCardModule, MemoryCardModule.class);
  return new DaggerSmartPhoneComponent(memoryCardModule);
}
```
</details>

이제  `MainActivity`에서 `SmartPhoneComponent`를 `builder()`로 생성하여 모듈에 값을 넘겨주어 초기화 의존성을 주입하면 됩니다. 
```kotlin
DaggerSmartPhoneComponent.builder()
    .memoryCardModule(MemoryCardModule(1024))
    .build()
    .inject(this)
smartPhone.makeACallWithRecording()
```


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
https://dagger.dev/dev-guide/ <br>
