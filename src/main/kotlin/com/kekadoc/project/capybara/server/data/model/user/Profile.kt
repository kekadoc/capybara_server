package com.kekadoc.project.capybara.server.data.model.user

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.Serializable

/**
 * То что знает только сервер и владелец профиля
 */
@Serializable
data class Profile(
    val login: String = emptyString(),
    val person: Person = Person.Empty,
    val type: ProfileType = ProfileType.DEFAULT
) {
    companion object {
        val Empty = Profile()
    }
}