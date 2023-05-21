package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileResponseDto(
    @SerialName("profile")
    val profile: ProfileDto,
)