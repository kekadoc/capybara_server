package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddresseeDto(
    @SerialName("user_id")
    @Contextual
    val userId: Identifier,
    @SerialName("profile")
    val profile: ProfileDto,
)