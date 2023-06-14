package com.kekadoc.project.capybara.server.domain.model.auth

data class Authorization(
    val accessToken: String,
    val refreshToken: String,
)