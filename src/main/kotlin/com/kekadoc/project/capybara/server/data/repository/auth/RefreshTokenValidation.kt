package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.domain.model.Identifier

sealed class RefreshTokenValidation {
    data class Valid(
        val userId: Identifier,
        val login: String,
    ) : RefreshTokenValidation()
    object Expired : RefreshTokenValidation()
    data class Invalid(
        val error: Throwable? = null
    ) : RefreshTokenValidation()
}