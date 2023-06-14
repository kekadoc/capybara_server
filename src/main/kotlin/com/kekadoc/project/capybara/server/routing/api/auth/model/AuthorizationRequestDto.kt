package com.kekadoc.project.capybara.server.routing.api.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationRequestDto(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
)