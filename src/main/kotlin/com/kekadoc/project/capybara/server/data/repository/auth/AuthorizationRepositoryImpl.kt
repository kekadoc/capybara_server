package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.data.source.api.auth.AuthorizationDataSource
import com.kekadoc.project.capybara.server.data.source.api.auth.RegistrationDataSource
import com.kekadoc.project.capybara.server.domain.model.Authorization
import com.kekadoc.project.capybara.server.domain.model.Identifier
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import com.kekadoc.project.capybara.server.domain.model.auth.registration.GetAllRegistrationRequestsResponse
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequest
import com.kekadoc.project.capybara.server.domain.model.auth.registration.RegistrationRequestInfo
import com.kekadoc.project.capybara.server.domain.model.auth.registration.UpdateRegistrationStatusRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class AuthorizationRepositoryImpl(
    private val authorizationDataSource: AuthorizationDataSource,
    private val registrationDataSource: RegistrationDataSource,
) : AuthorizationRepository {

    override fun authorizeUser(user: User): Flow<Authorization> = flow {
        emit(authorizationDataSource.authorizeUser(user.id, user.login))
    }

    override fun fetchUser(accessToken: Token): Flow<AccessTokenValidation> = flowOf {
        authorizationDataSource.fetchUser(accessToken)
    }

    override fun validateRefreshToken(
        refreshToken: String,
    ): Flow<RefreshTokenValidation> = flow {
        emit(authorizationDataSource.validateRefreshToken(refreshToken))
    }

    override fun registration(
        request: RegistrationRequest,
    ): Flow<RegistrationRequestInfo> = flowOf {
        registrationDataSource.registration(
            request = request,
        )
    }

    override fun getRegistrationStatus(
        registrationId: Identifier,
    ): Flow<RegistrationRequestInfo> = flowOf {
        registrationDataSource.getRegistrationStatus(
            registrationId = registrationId,
        )
    }

    override suspend fun updateRegistrationStatus(
        registrationId: Identifier,
        request: UpdateRegistrationStatusRequest,
    ): Flow<RegistrationRequestInfo> = flowOf {
        registrationDataSource.updateRegistrationStatus(
            registrationId = registrationId,
            request = request,
        )
    }

    override suspend fun getAllRegistrationRequests(

    ): Flow<GetAllRegistrationRequestsResponse> = flowOf {
        registrationDataSource.getAllRegistrationRequests()
    }

}