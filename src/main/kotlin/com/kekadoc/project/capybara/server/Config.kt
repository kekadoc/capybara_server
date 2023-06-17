package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.model.ScheduleSource
import com.kekadoc.project.capybara.server.di.Di
import io.ktor.server.application.Application
import io.ktor.server.config.*
import org.koin.core.component.get

object Config {

    const val VERSION = "1.2.0"
    const val API_KEY_HEADER = "ApiKey"
    const val API_KEY_QUERY = "apiKey"

    val applicationConfig: ApplicationConfig
        get() = Di.get<Application>().environment.config

    fun isSourceAvailable(source: ScheduleSource): Boolean {
        return when(source) {
            ScheduleSource.CHF_PNIPU_EXTRAMURAL -> false
            else -> true
        }
    }

    val publicApi: String
        get() = applicationConfig.getString("ktor.deployment.publicApi")

    val isDebug: Boolean
        get() = applicationConfig.getBoolean("debug.isDebug")

    val isDebugCreateMockData: Boolean
        get() = applicationConfig.getBoolean("debug.isCreateMockData")

    val isDebugDefaultSimplePassword: Boolean
        get() = applicationConfig.getBoolean("debug.defaultSimplePassword")

    val isDebugEncryptPassword: Boolean
        get() = applicationConfig.getBoolean("debug.encryptPassword")

}

fun ApplicationConfig.getBoolean(path: String): Boolean = property(path).getString().toBooleanStrict()

fun ApplicationConfig.getString(path: String): String = property(path).getString()