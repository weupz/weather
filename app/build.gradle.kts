import java.util.Properties

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

    val secret = rootProject.file("secret.properties").inputStream()
        .use { Properties().apply { load(it) } }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file(secret["keystore"]!!)
            storePassword = secret.getProperty("keystore_password")
            keyAlias = secret.getProperty("key_alias")
            keyPassword = secret.getProperty("key_password")
        }
        create("release") {
            storeFile = rootProject.file(secret["keystore"]!!)
            storePassword = secret.getProperty("keystore_password")
            keyAlias = secret.getProperty("key_alias")
            keyPassword = secret.getProperty("key_password")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
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
