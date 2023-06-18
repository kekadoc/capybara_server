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

val Profile.officialName: String
    get() = name + patronymic?.let { " $it" }

val Profile.shortName: String
    get() = buildString {
        append(surname)
        surname.take(1).takeIf { it.isNotEmpty() }?.let { append(" $it.") }
        patronymic?.take(1)?.takeIf { it.isNotEmpty() }?.let { append(" $it.") }
    }