package com.kekadoc.project.capybara.server.domain.model.user

data class Profile(
    val type: Type,
    val name: String,
    val surname: String,
    val patronymic: String? = null,
    val about: String? = null,
) {

    enum class Type {
        USER,
        SPEAKER,
        ADMIN,
    }

}