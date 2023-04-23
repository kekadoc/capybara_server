package com.kekadoc.project.capybara.server.data.model

data class AuthToken(
    val userId: Identifier,
    val token: String,
    val createdAt: Long,
)