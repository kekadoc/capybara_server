package com.kekadoc.project.capybara.server.data.model.user

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.Serializable

/**
 * То что знает только сервер о пользователе
 */
@Serializable
data class User(
    val id: Identifier = emptyString(),
    val profile: Profile = Profile.Empty,
    val password: String = emptyString(),
    val authToken: String = emptyString(),
    val pushToken: String = emptyString(),
    val communications: Communications = Communications.Empty,
    val groups: List<String> = emptyList(),
)

