buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        val kotlinVersion = WeatherProject.version.kotlin
        val androidGradleVersion = WeatherProject.version.androidGradle
        val jetifier = WeatherProject.dependencies.jetifier
        classpath("com.android.tools.build:gradle:$androidGradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath(jetifier)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
    }
    configurations.all {
        resolutionStrategy {
            val dependencies = WeatherProject.dependencies
            force(
                dependencies.appCompat,
                dependencies.lifecycleCommon,
                dependencies.lifecycleRuntime,
                dependencies.collection,
                dependencies.archCoreCommon,
                dependencies.annotation,
                dependencies.daggerAndroidCompiler,
                dependencies.jetifier,
                dependencies.okHttp
            )
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
