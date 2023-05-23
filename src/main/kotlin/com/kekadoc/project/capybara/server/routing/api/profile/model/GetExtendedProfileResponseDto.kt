package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.routing.model.profile.ExtendedProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetExtendedProfileResponseDto(
    @SerialName("profile")
    val profile: ExtendedProfileDto,
)