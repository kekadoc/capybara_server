package com.kekadoc.project.capybara.server.data.model

data class Contact(
    val id: Identifier,
    val user: User,
    val type: Type,
) {

    enum class Type {
        DEFAULT,
        PUBLIC,
    }

}