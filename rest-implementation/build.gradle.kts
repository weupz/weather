plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

android {
    val app = WeatherProject.android

    compileSdkVersion(app.compileSdkVersion)

    defaultConfig {
        minSdkVersion(app.minSdkVersion)
        targetSdkVersion(app.targetSdkVersion)
        versionCode = app.versionCode
        versionName = app.versionName

        testInstrumentationRunner = app.testInstrumentationRunner
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/source/buildconfigkotlin/src/main")
        }
    }

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    libraryVariants.all {
        generateBuildConfig.enabled = false
    }
}

buildConfigKotlin {
    sourceSet("main") {
        packageName("weather.rest")
        val appId = System.getenv("OPEN_WEATHER_MAP_APP_ID")?.takeIf { it.isNotEmpty() }
            ?: project.properties["OPEN_WEATHER_MAP_APP_ID"] as String?
            ?: throw NullPointerException(
                "Provide OPEN_WEATHER_MAP_APP_ID as environment variable or project property"
            )
        buildConfig("OPEN_WEATHER_MAP_APP_ID", appId)
    }
}

dependencies {
    val dependencies = WeatherProject.dependencies

    api(project(":rest"))

    implementation(dependencies.kotlin)
    implementation(dependencies.retrofit)
    implementation(dependencies.retrofitRxJava)
    implementation(dependencies.retrofitMoshi)
    implementation(dependencies.okHttp)

    implementation(dependencies.dagger)
    kapt(dependencies.daggerCompiler)

    testImplementation(dependencies.junit)
    androidTestImplementation(dependencies.testRunner)
    androidTestImplementation(dependencies.espressoCore)
}
