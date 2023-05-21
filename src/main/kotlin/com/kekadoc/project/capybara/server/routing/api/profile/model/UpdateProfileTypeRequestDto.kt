package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.source.network.model.ProfileTypeDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileTypeRequestDto(
    @SerialName("type")
    val type: ProfileTypeDto,
)