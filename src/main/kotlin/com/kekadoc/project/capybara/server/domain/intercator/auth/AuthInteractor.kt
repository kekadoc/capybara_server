package com.kekadoc.project.capybara.server.domain.intercator.auth

import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.routing.api.auth.model.*

interface AuthInteractor {

    suspend fun authorize(request: AuthorizationRequestDto): AuthorizationResponseDto

    suspend fun refreshToken(request: RefreshTokensRequestDto): AuthorizationResponseDto

    suspend fun registration(request: RegistrationRequestDto): RegistrationStatusResponseDto

    suspend fun getRegistrationStatus(registrationId: Identifier): RegistrationStatusResponseDto

    suspend fun cancelRegistrationRequest(registrationId: Identifier): RegistrationStatusResponseDto

    suspend fun updateRegistrationStatus(
        authToken: Token,
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequestDto,
    ): UpdateRegistrationStatusResponseDto

    suspend fun getAllRegistrationRequests(
        authToken: Token,
    ): GetAllRegistrationRequestsResponseDto

    suspend fun registrationConfirmEmail(
        token: Token,
    ): String

}