plugins {
    id("com.android.library")
    kotlin("android")
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

    implementation(project(":scheduler"))

    implementation(dependencies.kotlin)
    api(dependencies.recyclerview)
    api(dependencies.recyclerviewAdapter)

    testImplementation(dependencies.junit)
    androidTestImplementation(dependencies.testRunner)
    androidTestImplementation(dependencies.espressoCore)
}
