package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.domain.model.Identifier

sealed class AccessTokenValidation {
    data class Valid(
        val login: String,
        val userId: Identifier,
    ) : AccessTokenValidation()
    object Expired : AccessTokenValidation()
    data class Invalid(
        val error: Throwable? = null
    ) : AccessTokenValidation()
}