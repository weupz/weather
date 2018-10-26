plugins {
    id("java-library")
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    val dependencies = WeatherProject.dependencies
    api(dependencies.kotlin)
    api(dependencies.threeTenBpNoTzdb)
    api(dependencies.rxJava)
    api(dependencies.retrofit)
    implementation(dependencies.moshi)
    kapt(dependencies.moshiKotlinCodegen)
}
