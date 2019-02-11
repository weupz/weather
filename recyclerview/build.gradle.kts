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
            isMinifyEnabled = true
            consumerProguardFile("proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    libraryVariants.all {
        generateBuildConfigProvider {
            enabled = false
        }
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
