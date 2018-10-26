plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    val app = WeatherProject.android

    compileSdkVersion(app.compileSdkVersion)

    defaultConfig {
        applicationId = app.applicationId
        minSdkVersion(app.minSdkVersion)
        targetSdkVersion(app.targetSdkVersion)
        versionCode = app.versionCode
        versionName = app.versionName
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isShrinkResources = true
        }
        getByName("debug") {
        }
    }

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    val dependencies = WeatherProject.dependencies

    implementation(project(":base"))
    implementation(project(":featureweather"))

    implementation(dependencies.lifecycleRuntime)
    implementation(dependencies.lifecycleCommon)
    implementation(dependencies.collection)
    implementation(dependencies.archCoreCommon)
    compileOnly(dependencies.annotation)
}
