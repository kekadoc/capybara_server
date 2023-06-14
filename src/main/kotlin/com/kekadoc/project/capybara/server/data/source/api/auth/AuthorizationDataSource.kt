package com.kekadoc.project.capybara.server.data.source.api.auth

import com.kekadoc.project.capybara.server.data.repository.auth.AccessTokenValidation
import com.kekadoc.project.capybara.server.data.repository.auth.RefreshTokenValidation
import com.kekadoc.project.capybara.server.domain.model.auth.Authorization
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token

interface AuthorizationDataSource {

    /**
     * Механиз генерации авторизационных доступов для пользователя
     */
    suspend fun authorizeUser(
        userId: Identifier,
        login: String,
    ): Authorization

    /**
     * Проверка аксес токена
     */
    suspend fun fetchUser(accessToken: Token): AccessTokenValidation

    suspend fun validateRefreshToken(
        refreshToken: String
    ): RefreshTokenValidation

}