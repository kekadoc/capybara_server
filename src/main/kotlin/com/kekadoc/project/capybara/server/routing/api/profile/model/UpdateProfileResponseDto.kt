package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.source.network.model.ExtendedProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileResponseDto(
    @SerialName("profile")
    val profile: ExtendedProfileDto,
)