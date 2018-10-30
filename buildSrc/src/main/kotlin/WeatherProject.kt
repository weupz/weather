object WeatherProject {

    object android {
        const val buildToolsVersion = "28.0.2"
        const val minSdkVersion = 23
        const val compileSdkVersion = 28
        const val targetSdkVersion = 28
        const val applicationId = "cf.tukang.weather"
        const val versionCode = 1
        const val versionName = "0.0.1"
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    object version {
        const val androidGradle = BuildConfig.androidGradleVersion
        const val androidx = "1.0.0"
        const val lifecycle = "2.0.0"
        const val kotlin = BuildConfig.kotlinVersion
        const val threeTenBp = "1.3.7"
        const val threeTenAbp = "1.1.1"
        const val rxJava = "2.2.2"
        const val rxAndroid = "2.1.0"
        const val okHttp = "3.11.0"
        const val retrofit = "2.4.0"
        const val moshi = "1.7.0"
        const val dagger = "2.18"
        const val glide = "4.8.0"
    }

    object dependencies {
        const val lifecycleCommon = "androidx.lifecycle:lifecycle-common:${version.lifecycle}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:${version.lifecycle}"
        const val archCoreCommon = "androidx.arch.core:core-common:${version.lifecycle}"
        const val appCompat = "androidx.appcompat:appcompat:${version.androidx}"
        const val fragment = "androidx.fragment:fragment:${version.androidx}"
        const val recyclerview = "androidx.recyclerview:recyclerview:${version.androidx}"
        const val material = "com.google.android.material:material:${version.androidx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val collection = "androidx.collection:collection:${version.androidx}"
        const val annotation = "androidx.annotation:annotation:${version.androidx}"
        const val jetifier = "com.android.tools.build.jetifier:jetifier-processor:1.0.0-beta02"

        object databinding {
            const val compiler = "androidx.databinding:compiler:${version.androidGradle}"
            const val common = "androidx.databinding:databinding-common:${version.androidGradle}"
            const val runtime = "androidx.databinding:databinding-runtime:${version.androidGradle}"
        }

        const val testRunner = "androidx.test:runner:1.1.0-alpha4"
        const val testRules = "androidx.test:rules:1.1.0-alpha4"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.1.0-alpha4"
        const val junit = "junit:junit:4.12"

        const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${version.kotlin}"
        const val threeTenBp = "org.threeten:threeten:${version.threeTenBp}"
        const val threeTenBpNoTzdb = "org.threeten:threetenbp:${version.threeTenBp}:no-tzdb"
        const val threeTenAbp = "com.jakewharton.threetenabp:threetenabp:${version.threeTenAbp}"
        const val rxJava = "io.reactivex.rxjava2:rxjava:${version.rxJava}"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:${version.rxAndroid}"
        const val okHttp = "com.squareup.okhttp3:okhttp:${version.okHttp}"
        const val retrofit = "com.squareup.retrofit2:retrofit:${version.retrofit}"
        const val retrofitRxJava = "com.squareup.retrofit2:adapter-rxjava2:${version.retrofit}"
        const val retrofitMoshi = "com.squareup.retrofit2:converter-moshi:${version.retrofit}"
        const val moshi = "com.squareup.moshi:moshi:${version.moshi}"
        const val moshiKotlinCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${version.moshi}"
        const val dagger = "com.google.dagger:dagger:${version.dagger}"
        const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${version.dagger}"
        const val daggerCompiler = "com.google.dagger:dagger-compiler:${version.dagger}"
        const val daggerAndroidCompiler = "com.google.dagger:dagger-android-processor:${version.dagger}"
        const val recyclerviewAdapter = "com.hannesdorfmann:adapterdelegates4:4.0.0"
        const val iconics = "com.mikepenz:iconics-core:3.1.0"
        const val iconicsWeather = "com.mikepenz:weather-icons-typeface:2.0.10.5@aar"

        object Glide {
            const val glide = "com.github.bumptech.glide:glide:${version.glide}"
            const val compiler = "com.github.bumptech.glide:compiler:${version.glide}"
            const val okHttp = "com.github.bumptech.glide:okhttp3-integration:${version.glide}"
        }

        @JvmField val glide = Glide
    }
}
