buildscript {
    repositories {
        gradlePluginPortal()
    }
}

repositories {
    google()
    gradlePluginPortal()
    jcenter()
}

plugins {
    kotlin("jvm") version "1.3.10"
    id("java-gradle-plugin")
    id("com.github.kukuhyoniatmoko.buildconfigkotlin") version "1.0.5"
}

gradlePlugin {
    plugins {
        create("myPlugins") {
            id = "com.squareup.sqldelight.android"
            implementationClass = "com.squareup.sqldelight.gradle.SqlDelightPlugin"
        }
    }
}

buildConfigKotlin {
    sourceSet("main") {
        buildConfig(name = "kotlinVersion", value = "1.3.10")
        buildConfig(name = "androidGradleVersion", value = "3.2.1")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation("com.android.tools.build:gradle:3.2.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.10")
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.10")
    implementation("com.android.tools.build.jetifier:jetifier-processor:1.0.0-beta02")
    api("com.squareup.sqldelight:gradle-plugin:1.0.0-rc2")
}
