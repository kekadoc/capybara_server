package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Identifier
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.model.User
import kotlinx.coroutines.flow.Flow
import java.sql.Ref

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