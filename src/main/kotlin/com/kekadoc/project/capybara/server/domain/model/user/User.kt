package com.kekadoc.project.capybara.server.domain.model.user

import com.kekadoc.project.capybara.server.domain.model.Identifier

data class User(
    val id: Identifier,
    val login: String,
    val password: String,
    val type: Profile.Type,
    val status: UserStatus,
    val name: String,
    val surname: String,
    val patronymic: String,
    val about: String?,
    val communications: Communications,
    val groupIds: List<Identifier>
)

fun User.isAdmin(): Boolean = type == Profile.Type.ADMIN

val User.profile: Profile
    get() = Profile(
        type = type,
        name = name,
        surname = surname,
        patronymic = patronymic,
        about = about,
    )