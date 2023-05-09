package com.kekadoc.project.capybara.server.data.source.database

data class DatabaseConfig(
    val hostName: String,
    val port: Int,
    val database: String,
    val username: String,
    val password: String,
    val internalUrl: String,
    val externalUrl: String,
)