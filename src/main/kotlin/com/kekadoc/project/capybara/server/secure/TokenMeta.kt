package com.kekadoc.project.capybara.server.secure

import com.kekadoc.project.capybara.server.data.model.Identifier

data class TokenMeta(
    val value: String,
    val userId: Identifier,
    val expiresAt: Long,
)