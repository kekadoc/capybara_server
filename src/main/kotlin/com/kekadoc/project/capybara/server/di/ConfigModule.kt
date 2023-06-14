package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.common.secure.JWTConfig
import com.kekadoc.project.capybara.server.data.source.api.notifications.email.EmailNotificationConfig
import com.kekadoc.project.capybara.server.data.source.database.DatabaseConfig
import io.ktor.server.config.*
import org.koin.dsl.module
import kotlin.time.Duration.Companion.hours

val configModule = module {

    single<JWTConfig>(createdAtStart = true) {
        val ktorApp = ktorApplication()
        JWTConfig(
            secret = ktorApp.environment.config.propertyOrNull("jwt.secret")?.getString().orEmpty(),
            issuer = ktorApp.environment.config.propertyOrNull("jwt.issuer")?.getString().orEmpty(),
            audience = ktorApp.environment.config.propertyOrNull("jwt.audience")?.getString().orEmpty(),
            accessLifetimeHours = ktorApp.environment.config.propertyOrNull("jwt.access.lifetime")?.getString()?.toLong() ?: 1.hours.inWholeHours,
            refreshLifetimeHours = ktorApp.environment.config.propertyOrNull("jwt.refresh.lifetime")?.getString()?.toLong() ?: (24 * 7).hours.inWholeHours,
        )
    }

    single<DatabaseConfig>(createdAtStart = true) {
        val application = get<io.ktor.server.application.Application>()
        val config: ApplicationConfig = application.environment.config
        DatabaseConfig(
            port = config.propertyOrNull("database.port")?.getString().let { port ->
                port?.toIntOrNull() ?: throw RuntimeException("Incorrect Database port=$port")
            },
            username = config.propertyOrNull("database.username")?.getString()
                ?: throw RuntimeException("Username for Database not found"),
            password = config.propertyOrNull("database.password")?.getString()
                ?: throw RuntimeException("Password for Database not found"),
            internalUrl = config.propertyOrNull("database.url")?.getString()
                ?: throw RuntimeException("InternalUrl for Database not found"),
        )
    }

    single<EmailNotificationConfig>(createdAtStart = true) {
        val application = get<io.ktor.server.application.Application>()
        val config = application.environment.config
        EmailNotificationConfig(
            tokenSecret = config.propertyOrNull("email.secret")?.getString()
                ?: throw RuntimeException("email.secret is null"),
            hostName = config.propertyOrNull("email.hostName")?.getString()
                ?: throw RuntimeException("email.hostName is null"),
            smtpPort = config.propertyOrNull("email.smtpPort")?.getString()?.toIntOrNull()
                ?: throw RuntimeException("email.smtpPort is null"),
            username = config.propertyOrNull("email.username")?.getString()
                ?: throw RuntimeException("email.username is null"),
            password = config.propertyOrNull("email.password")?.getString()
                ?: throw RuntimeException("email.password is null"),
            fromEmail = config.propertyOrNull("email.fromEmail")?.getString()
                ?: throw RuntimeException("email.fromEmail is null"),
            subject = config.propertyOrNull("email.subject")?.getString()
                ?: throw RuntimeException("email.subject is null"),
        )
    }

}