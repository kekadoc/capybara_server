package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.common.extensions.flowOf
import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Token
import com.kekadoc.project.capybara.server.data.model.User
import com.kekadoc.project.capybara.server.data.source.api.auth.AuthorizationDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

class AuthorizationRepositoryImpl(
    private val authorizationDataSource: AuthorizationDataSource,
) : AuthorizationRepository {

    override fun authorizeUser(user: User): Flow<Authorization> {
        return flow {
            emit(authorizationDataSource.authorizeUser(user.id, user.login))
        }
    }

    override fun fetchUser(accessToken: Token): Flow<AccessTokenValidation> = flowOf {
        authorizationDataSource.fetchUser(accessToken)
    }

    override fun validateRefreshToken(refreshToken: String): Flow<RefreshTokenValidation> {
        return flow {
            emit(authorizationDataSource.validateRefreshToken(refreshToken))
        }
    }

}