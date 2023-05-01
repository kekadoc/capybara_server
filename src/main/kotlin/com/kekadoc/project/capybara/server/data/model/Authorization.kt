package com.kekadoc.project.capybara.server.data.model

data class Authorization(
    val accessToken: String,
    val refreshToken: String,
)