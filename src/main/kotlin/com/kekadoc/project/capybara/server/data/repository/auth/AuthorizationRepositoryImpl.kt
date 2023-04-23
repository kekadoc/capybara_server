package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

class AuthorizationRepositoryImpl : AuthorizationRepository {

    override fun authorizeUser(login: String, password: String): Flow<Authorization> {
        TODO("Not yet implemented")
    }

    override fun updateUserAuthToken(userId: Identifier, authToken: String): Flow<Authorization> {
        TODO("Not yet implemented")
    }

    override fun deleteAuthorization(userId: Identifier): Flow<Unit> {
        TODO("Not yet implemented")
    }


}