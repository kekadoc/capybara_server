package com.kekadoc.project.capybara.server.data.model

data class User(
    val id: Identifier,
    val login: String,
    val password: String,
    val profile: Profile,
    val communications: Communications,
    val groupIds: List<Identifier>
)