package com.kekadoc.project.capybara.server.data.source.api.auth

import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.repository.auth.AccessTokenValidation
import com.kekadoc.project.capybara.server.data.repository.auth.RefreshTokenValidation
import kotlinx.coroutines.flow.Flow

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