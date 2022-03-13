# DependencyInjectionDemo
Dependency Injection With Dagger2 (https://www.udemy.com/course/android-architecture-componentsmvvm-with-dagger-retrofit/)

# Scenario
![image](https://user-images.githubusercontent.com/55622345/158050431-a32ca0e1-8cb4-44cd-b1ed-8dbe2498b468.png)
SmartPhoen 객체는 Battery, MemoryCard, SIMCard에 의존,
SIMCard는 ServiceProvider에 의존.
SmartPhone 객체 생성 시 필요한 의존성 주입을 Dagger로 주입

# Injection Process 
SmartPhone Dependeny Injection Progress 
1. SmartPhoneApplication - smartPhoneComponent = initDagger()
  android:name에 등록된 Application부터 실행, initDagger 함수 실행 
2, DaggerSmartPhoneComponent.builder().memoryCardModule(MemoryCardModule(1000))
  DaggerSmartPhoneComponent 인스턴스 생성(builder pattern), 
3. DaggerSmartPhoneComponent - memoryCardModule : this.memoryCardModule = Preconditions.checkNotNull(memoryCardModule);
  memoryCardModule 주입, memoryCardModule = null
4. DaggerSmartPhoneComponent.builder().memoryCardModule().build()
  SmartPhoneComponent build()
5. DaggerSmartPhoneComponent - build() : new DaggerSmartPhoneComponet
  Checks that the component builder field {@code requirement} has been initialized, 
  new DaggerSmartPhoneComponent(memoryCardModule) 반환
6. DaggerSmartPhoneComponent - DaggerSmartPhoneComponent : initialize(memoryCardModuleParam)
  DaggerSmartPhoneComponent initialize
7. DaggerSmartPhoneComponent - initialize() - this.sIMCardProvider = SIMCard_Factory.create(ServiceProvider_Factory.create());
  ServiceProvider_Factory.create() 실행
8. ServiceProvider_Factory - InstanceHolder - private static final ServiceProvider_Factory INSTANCE = new ServiceProvider_Factory();
  SIMCard_Factory create, Factory Pattern으로 IINSTANCE(ServiceProvider_Factory) 생성자 반환 
9. SIMCard_Factory - create :  return new SIMCard_Factory(serviceProvider);
10. SIMCard_Factory - SIMCard_Factory : this.serviceProvider = serviceProvider; 
  반환받은 serviceProvider 생성자를 argument로 SIMCard_Factory 생성 후 반환 
11. DaggerSmartPhoneComponent - initialize() - this.providersMemoryCardProvider = MemoryCardModule_ProvidersMemoryCardFactory.create(memoryCardModuleParam);
  3번에서 생성된 memoryCardModule[memorySize = 1000]로 MemoryCardModule_ProvidersMemoryCardFactory.create 호출
12. DaggerSmartPhoneComponent - return new MemoryCardModule_ProvidersMemoryCardFactory(module);
  MemoryCardModule_ProvidersMemoryCardFactory 생성자 실행
13. MemoryCardModule_ProvidersMemoryCardFactory - MemoryCardModule_ProvidersMemoryCardFactory : this.module = module; 
  module 전역변수 설정, DaggerSmartPhoneComponent의 providersMemoryCardProvider에 MemoryCardModule_ProvidersMemoryCardFactory 주입, 12번 반환
14.  DaggerSmartPhoneComponent - initialize() - this.smartPhoneProvider = DoubleCheck.provider(SmartPhone_Factory.create(((Provider) NickelCadmiumBattery_Factory.create()), sIMCardProvider, providersMemoryCardProvider));
  smartPhoneProvider 생성을 위해 battery 생성, battery는 NickelCadmiumBattery_Factory.create 호출
15. NickelCadmiumBattery_Factory - create() : return InstanceHolder.INSTANCE;
  NickelCadmiumBattery_Factory 생성자 반환
16. DaggerSmartPhoneComponent - initialize() - this.smartPhoneProvider = DoubleCheck.provider(SmartPhone_Factory.create(...)
  15에서 생성된 Battery, 10에서 생성된 SIMCard, 13에서 생성된 MemoryCard로 SmartPhone Factory 실행
17. SmartPhone_Factory - create : return new SmartPhone_Factory(batteryProvider, simCardProvider, memoryCardProvider);
  SmartPhone_Factory 클래스 내부에서 SmartPhone_Factory 객체 생성
18. SmartPhone_Factory - SmartPhone_Factory : 
    this.batteryProvider = batteryProvider;    
    this.simCardProvider = simCardProvider;
    this.memoryCardProvider = memoryCardProvider;
  각 객체 전역변수에 할당 후 17번에서 객체 리턴
19. MainActivity - onCreate : (application as SmartPhoneApplication).smartPhoneComponent.inject(this)
  application을 SmartPhoneApplication으로 할당 후 종속성 주입 
  [inject() from interface SmartPhoneComponent]
20. DaggerSmartPhoneComponent - inject : injectMainActivity(mainActivity);
  내부적으로 생성된 injectMainActivty 함수 실행
21. DaggerSmartPhoneComponent - injectMainActivity : MainActivity_MembersInjector.injectSmartPhone(instance, smartPhoneProvider.get());
  Field injection으로 생성된 MainActivity_MembersInjector에서 Provider로 smartPhone MainActivity에 인젝션 주입 함수 실행 
  smartPhoneProvider.get()부터 실행됨
22. SmartPhone - get : return newInstance(batteryProvider.get(), simCardProvider.get(), memoryCardProvider.get());
  18번에서 생성된 전역변수로 새 SmartPhone 인스턴스 생성
  batteryProvider.get(), 
  simCardProvider.get(), 
  memoryCardProvider.get() 순차적으로 실행
23. NickelCadmiumBattery_Factory - get : return newInstance(); 
  22번의 batteryProvider.get() 실행, NickelCadmiumBattery_Factory의  newInstance 함수 실행
24. NickelCadmiumBattery_Factory - newInstance : return new NickelCadmiumBattery();
  새 NickelCadmiumBattery 인스턴스 반환 
25. SIMCard_Factory - get : return newInstance(serviceProvider.get());
  22번의 simCardProvider.get() 실행,  newInstance 함수 실행
26. ServiceProvider_Factory - get : return newInstance();
  SIMCard는 ServiceProvider에 종속성을 가지므로 ServiceProvider의 newInstance 함수 실행
27. ServiceProvider_Factory - newInstance : return new ServiceProvider();
  ServiceProvider 생성자 실행 
28. ServiceProvider - init : Log.i("MYTAG","Service Provider Constructed")
  ServiceProvider 생성자 로그 출력 
29. SIMCard_Factory - newInstance : return new SIMCard(serviceProvider);
  SIMCard 생성자 실행
30. SIMCard - init : Log.i("MYTAG","SIM Card Constructed")
  SIMCard 생성자 로그 출력
31. MemoryCardModule_ProvidersMemoryCardFactory - get : return providersMemoryCard(module);
  22번의 memoryCardProvider.get() 실행, providersMemoryCard 함수 실행, module은 초기 SmartPhoneApplication이 실행 될 때 Module에서 정의된 값
32. MemoryCardModule_ProvidersMemoryCardFactory - providersMemoryCard : return Preconditions.checkNotNullFromProvides(instance.providersMemoryCard());
  MemoryCardModule의 providersMemoryCard실행, 
33. MemoryCardModule - providersMemoryCard : Log.i("MYTAG", "Size of the memory is $memorySize"); return MemoryCard()
  로그 출력 후 MemoryCard 생성자 실행 
34. MemoryCard - init : Log.i("MYTAG","Memory Card Constructed")
  MemoryCard 생성자 로그 출력, 33번에서 생성자 리턴.
35. SmartPhone - newInstance : return new SmartPhone(battery, simCard, memoryCard);
  22번의 newInstance에서 호출됨, SmartPhone 생성자 실행
36. SmartPhone - init : 
    Log.i("MYTAG", "SmartPhone Constructed")
    battery.getPower()
    simCard.getConnection()
    memoryCard.getSpaceAvailablity()
  SmartPhone 생성자 로그 출력. battery, simCard, memoryCard 각각의 함수 실행
37. MainActivity_MembersInjector - injectSmartPhone : instance.smartPhone = smartPhone;
  21번의 injectSmartPhone 함수 실행, MainActivity의 전역변수 smartPhone에 생성된 smartPhone 주입
38. DaggerSmartPhoneComponent - injectMainActivity : return instance;
  주입된 instance 반환
39 MainActivity - onCreate : smartPhone.makeACallWithRecording()
  makeACallWithRecording 함수 실행, 
40. SmartPhone - makeACallWithRecording : Log.i("MYTAG", "Calling.....")
  함수 로그 출력(onCreate에서 smartPhone 객체가 생성됨을 알림)
  
