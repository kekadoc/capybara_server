package com.kekadoc.project.capybara.server.common.secure

data class JWTConfig constructor(
    val secret: String,
    val emailSecret: String,
    val issuer: String,
    val audience: String,
    val realm: String,
    val accessLifetimeHours: Long,
    val refreshLifetimeHours: Long,
)