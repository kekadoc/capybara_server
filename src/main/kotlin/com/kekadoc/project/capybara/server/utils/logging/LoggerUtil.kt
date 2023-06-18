package com.kekadoc.project.capybara.server.utils.logging

import io.ktor.server.application.*

object LoggerUtil {

    fun logApp(application: Application) {
        val logMessage = buildString {
            append("Start application with:")
            appendLine()
            append("developmentMode=${application.developmentMode}")
            appendLine()
            append("environment.developmentMode=${application.environment.developmentMode}")
            appendLine()
            append("environment.rootPath=${application.environment.rootPath}")
            appendLine()
            val config = application.environment.config
            config.keys().forEach { key ->
                val value = try {
                    config.property(key).getString()
                } catch (e: Throwable) {
                    try {
                        config.property(key).getList().joinToString { it }
                    } catch (e: Throwable) {
                        null
                    }
                }
                append("environment.rootPath.config.$key=${value?.take(64)}")
                if ((value?.length ?: 0) > 64) append("...")
                appendLine()
            }
        }
        Logger.log().info(logMessage)
    }

}