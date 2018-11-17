plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
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

    compileOptions {
        setSourceCompatibility(JavaVersion.VERSION_1_8)
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    libraryVariants.all {
        generateBuildConfig.enabled = false
    }
}

dependencies {
    val dependencies = WeatherProject.dependencies

    implementation(project(":theme"))
    implementation(project(":recyclerview"))
    implementation(project(":scheduler"))
    implementation(project(":rest"))
    implementation(project(":di"))
    implementation(project(":mvi-lifecycle"))
    implementation(project(":imageloader"))

    implementation(dependencies.appCompat)
    implementation(dependencies.material)
    implementation(dependencies.constraintLayout)

    implementation(dependencies.kotlin)
    implementation(dependencies.iconics.core)
    implementation(dependencies.iconics.weather)
    implementation(dependencies.iconics.view)

    implementation(dependencies.dagger)
    implementation(dependencies.daggerAndroidSupport)
    kapt(dependencies.daggerCompiler)
    kapt(dependencies.daggerAndroidCompiler)

    testImplementation(dependencies.junit)
    androidTestImplementation(dependencies.testRunner)
    androidTestImplementation(dependencies.espressoCore)
}
