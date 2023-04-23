package com.kekadoc.project.capybara.server.data.repository.auth

import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

interface AuthorizationRepository {

    fun authorizeUser(
        login: String,
        password: String,
    ): Flow<Authorization>

    fun updateUserAuthToken(
        userId: Identifier,
        authToken: String,
    ): Flow<Authorization>

    fun deleteAuthorization(
        userId: Identifier,
    ): Flow<Unit>

}