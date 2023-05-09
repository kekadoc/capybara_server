package com.kekadoc.project.capybara.server.data.source.network.model

import com.kekadoc.project.capybara.server.domain.model.Identifier
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @Contextual
    @SerialName("id")
    val id: Identifier,
    @SerialName("type")
    val type: ProfileTypeDto,
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("patronymic")
    val patronymic: String,
    @SerialName("avatar")
    val avatar: String?,
    @SerialName("about")
    val about: String?,
)