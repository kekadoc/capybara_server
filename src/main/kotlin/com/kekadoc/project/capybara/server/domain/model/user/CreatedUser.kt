package com.kekadoc.project.capybara.server.domain.model.user

data class CreatedUser(
    val user: User,
    val tempPass: String,
)