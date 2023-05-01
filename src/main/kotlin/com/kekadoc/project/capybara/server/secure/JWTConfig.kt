package com.kekadoc.project.capybara.server.secure

data class JWTConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val accessLifetimeHours: Long,
    val refreshLifetimeHours: Long,
)