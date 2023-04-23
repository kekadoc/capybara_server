package com.kekadoc.project.capybara.server.routing.api.profile.model

import com.kekadoc.project.capybara.server.common.extensions.emptyString
import com.kekadoc.project.capybara.server.data.source.network.model.ProfileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProfileRequest(
    @SerialName("login")
    val login: String = emptyString(),
    @SerialName("type")
    val type: ProfileDto.Type,
    @SerialName("name")
    val name: String,
    @SerialName("surname")
    val surname: String,
    @SerialName("patronymic")
    val patronymic: String,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("role")
    val role: String,
    @SerialName("about")
    val about: String,
)