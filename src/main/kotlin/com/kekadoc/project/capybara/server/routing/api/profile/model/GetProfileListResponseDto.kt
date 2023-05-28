package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.routing.model.profile.ProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetProfileListResponseDto(
    @SerialName("profiles")
    val profiles: List<ProfileDto>,
)