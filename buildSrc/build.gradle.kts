buildscript {
    repositories {
        gradlePluginPortal()
    }
}

repositories {
    gradlePluginPortal()
    jcenter()
}

plugins {
    kotlin("jvm") version "1.2.71"
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "kotlinVersion", value = "1.2.71")
        buildConfig(name = "androidGradleVersion", value = "3.2.1")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.71")
}
