package com.kekadoc.project.capybara.server.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenMeta(
    val userId: String,
    val token: String
)