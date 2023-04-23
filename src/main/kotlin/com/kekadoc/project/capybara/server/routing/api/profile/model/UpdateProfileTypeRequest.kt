package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileTypeRequest(
    val type: ProfileDto.Type,
)