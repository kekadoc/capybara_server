package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.model.user.Profile
import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileResponse(
    val profile: Profile,
    val password: String,
)