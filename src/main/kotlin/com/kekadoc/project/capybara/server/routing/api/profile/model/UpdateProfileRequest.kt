package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.model.user.Person
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    val person: Person,
)