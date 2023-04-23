package com.kekadoc.project.capybara.server.data.model

data class User(
    val id: Identifier,
    val login: String,
    val password: String,
    val profile: Profile,
    val character: UserCharacter,
    val communications: Communications,
    val availability: UserAvailability,
    val groups: List<Group>
)