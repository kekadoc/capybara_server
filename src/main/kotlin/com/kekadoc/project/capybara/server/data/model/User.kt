package com.kekadoc.project.capybara.server.data.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val profile: Profile = Profile.Empty,
    val authToken: String = emptyString(),
    val pushToken: String = emptyString(),
    val groupId: String? = null
)