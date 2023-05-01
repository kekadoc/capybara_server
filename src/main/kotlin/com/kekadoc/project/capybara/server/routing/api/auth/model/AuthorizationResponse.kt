package com.kekadoc.project.capybara.server.routing.api.auth.model

import com.kekadoc.project.capybara.server.data.model.Token
import kotlinx.serialization.Serializable

@Serializable
data class AuthorizationResponse(
    val accessToken: Token,
    val refreshToken: Token,
)