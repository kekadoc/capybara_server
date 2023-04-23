package com.kekadoc.project.capybara.server.data.model

data class Contact(
    val id: Identifier,
    val user: User,
    val communications: Communications
)