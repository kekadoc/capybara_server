package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddresseeDto(
    @SerialName("user_id")
    val userId: Identifier,
    @SerialName("profile")
    val profile: ProfileDto,
)