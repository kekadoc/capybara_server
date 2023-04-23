package com.kekadoc.project.capybara.server.data.source.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    @SerialName("type")
    val type: Type,
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
) {

    enum class Type {
        USER,
        SPEAKER,
        ADMIN,
        DEFAULT,
    }

}