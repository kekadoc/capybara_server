package com.kekadoc.project.capybara.server.data.model

data class Group(
    val id: Identifier,
    val name: String,
    val members: List<User>,
)