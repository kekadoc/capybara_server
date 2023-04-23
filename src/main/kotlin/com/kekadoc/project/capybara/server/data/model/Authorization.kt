package com.kekadoc.project.capybara.server.data.model

data class Authorization(
    val token: String,
    val user: User,
)