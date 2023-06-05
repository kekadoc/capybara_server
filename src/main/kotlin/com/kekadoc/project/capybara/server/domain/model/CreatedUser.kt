package com.kekadoc.project.capybara.server.domain.model

data class CreatedUser(
    val user: User,
    val tempPass: String,
)