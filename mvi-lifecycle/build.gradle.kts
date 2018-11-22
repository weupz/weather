plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val dependencies = WeatherProject.dependencies
    api(project(":mvi"))
    api(dependencies.lifecycleCommon)
    implementation(dependencies.dagger)
    kapt(dependencies.daggerCompiler)
}
