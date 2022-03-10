## Dokter Pribadimu for Android

Android app developed for BIMA Mobile's branch in Indonesia (http://www.bimamobile.com/)
It is a mobile app that provides health-care service for its users including features like: 
1. Scheduling and consultation with a real doctor via a phone call
2. News about various categories on health
3. Search for partners that offer discounts (pharmacies, hospitals, etc.)
4. Health record of the patient/app user, with the prescription given by the doctor

It is not active anymore, but you can take a look at it here: https://play.google.com/store/apps/details?id=com.bima.dokterpribadimu

### Important notes
This project is using [ProGuard](http://proguard.sourceforge.net/) to shrink and obfuscate the packaged code. If you're having problem while running, set `minifyEnabled false` in **app level** `build.gradle` for debug and release config

### Setup
- Android Studio
- Use Fabric Gradle plugins for Crashlytics support.
- Use Android API Level 23 to build. Install Android 6.0 (API 23) from Android SDK Manager.

### Libraries
- Android Support Library
- Android DataBinding
- [Retrofit](https://github.com/square/retrofit)
- [OkHttp](https://github.com/square/okhttp)
- [Gson](https://github.com/google/gson)
- [Dagger](https://github.com/square/dagger)
- [Picasso](https://github.com/square/picasso)
- [Calligraphy](https://github.com/chrisjenx/Calligraphy)
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/Rxandroid)
- [RxLifecycle](https://github.com/trello/RxLifecycle)
- [BindingCollectionAdapter](https://github.com/evant/binding-collection-adapter)
- [CircleImageView](https://github.com/hdodenhof/CircleImageView)
- Facebook Android SDK
- Google Play Service Auth (Google+)
