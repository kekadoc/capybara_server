package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.data.source.network.model.AuthorizedUserDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserCommunicationsResponseDto(
    @SerialName("profile")
    val profile: AuthorizedUserDto,
)