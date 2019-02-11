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
    implementation(dependencies.kotlin)

    implementation(dependencies.room.runtime)
    kapt(dependencies.room.compiler)
    implementation(dependencies.room.rx)
    implementation(dependencies.rxJava)
    implementation(dependencies.moshi)
    kapt(dependencies.moshiKotlinCodegen)

    implementation(dependencies.dagger)
    kapt(dependencies.daggerCompiler)

    testImplementation(dependencies.room.testing)
    testImplementation(dependencies.junit)
    androidTestImplementation(dependencies.testRunner)
    androidTestImplementation(dependencies.testRules)
    androidTestImplementation(dependencies.espressoCore)
}
