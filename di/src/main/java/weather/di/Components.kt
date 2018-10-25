package weather.di

import android.annotation.SuppressLint
import android.content.Context

const val COMPONENT_NAME = "@app:app-component"

@SuppressLint("WrongConstant")
inline fun <reified T> Context.component(): T {
    return applicationContext.getSystemService(COMPONENT_NAME) as T
}
