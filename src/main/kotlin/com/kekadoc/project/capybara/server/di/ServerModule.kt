package com.kekadoc.project.capybara.server.di

import com.kekadoc.project.capybara.server.common.secure.JWTConfig
import org.koin.dsl.module
import kotlin.time.Duration.Companion.hours

val serverModule = module {
    single<JWTConfig> {
        val ktorApp = ktorApplication()
        JWTConfig(
            secret = ktorApp.environment.config.propertyOrNull("jwt.secret")?.getString().orEmpty(),
            emailSecret = ktorApp.environment.config.propertyOrNull("jwt.email.secret")?.getString().orEmpty(),
            issuer = ktorApp.environment.config.propertyOrNull("jwt.issuer")?.getString().orEmpty(),
            audience = ktorApp.environment.config.propertyOrNull("jwt.audience")?.getString().orEmpty(),
            realm = ktorApp.environment.config.propertyOrNull("jwt.realm")?.getString().orEmpty(),
            accessLifetimeHours = ktorApp.environment.config.propertyOrNull("jwt.access.lifetime")?.getString()?.toLong() ?: 1.hours.inWholeHours,
            refreshLifetimeHours = ktorApp.environment.config.propertyOrNull("jwt.refresh.lifetime")?.getString()?.toLong() ?: (24 * 7).hours.inWholeHours,
        )
    }
}