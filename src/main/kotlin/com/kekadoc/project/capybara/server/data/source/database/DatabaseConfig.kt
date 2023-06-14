package com.kekadoc.project.capybara.server.data.source.database

data class DatabaseConfig(
    val port: Int,
    val username: String,
    val password: String,
    val internalUrl: String,
)