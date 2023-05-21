package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.domain.model.Authorization
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.domain.model.auth.registration.GetAllRegistrationRequestsResponse
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequest
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequestInfo
import com.kekadoc.project.capybara.server.domain.model.auth.registration.UpdateRegistrationStatusRequest
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

    /**
     *
     */
    fun registration(request: RegistrationRequest): Flow<RegistrationRequestInfo>

    /**
     *
     */
    fun getRegistrationStatus(registrationId: Identifier): Flow<RegistrationRequestInfo>

    /**
     *
     */
    suspend fun updateRegistrationStatus(
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequest,
    ): Flow<RegistrationRequestInfo>

    /**
     *
     */
    suspend fun getAllRegistrationRequests(): Flow<GetAllRegistrationRequestsResponse>

}