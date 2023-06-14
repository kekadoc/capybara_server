package com.kekadoc.project.capybara.server.common.secure

data class JWTConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val accessLifetimeHours: Long,
    val refreshLifetimeHours: Long,
)