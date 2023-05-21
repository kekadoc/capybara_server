package com.kekadoc.project.capybara.server.routing.api.auth.model

import com.kekadoc.project.capybara.server.domain.model.Token
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationResponse(
    @SerialName("accessToken")
    val accessToken: Token,
    @SerialName("refreshToken")
    val refreshToken: Token,
)