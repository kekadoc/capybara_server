package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.domain.model.Authorization
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Генерация токенов и их валидация
 */
interface AuthorizationRepository {

    /**
     * Создание авторизации для пользователя
     */
    fun authorizeUser(user: User): Flow<Authorization>

    /**
     * Проверка аксес токена
     */
    fun fetchUser(accessToken: Token): Flow<AccessTokenValidation>

    /**
     * Проверка рефреш токена
     */
    fun validateRefreshToken(refreshToken: String): Flow<RefreshTokenValidation>

}