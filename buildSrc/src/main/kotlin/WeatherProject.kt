object WeatherProject {

    object android {
        const val buildToolsVersion = "28.0.3"
        const val minSdkVersion = 23
        const val compileSdkVersion = 28
        const val targetSdkVersion = 28
        const val applicationId = "cf.tukang.weather"
        const val versionCode = 2
        const val versionName = "0.0.2-beta1"
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    @JvmField val dependencies = Dependencies

    @JvmField val version = dependencies.version
}
