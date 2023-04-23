package com.kekadoc.project.capybara.server.data.source.api.auth

import com.kekadoc.project.capybara.server.data.model.Authorization
import com.kekadoc.project.capybara.server.data.model.Identifier
import kotlinx.coroutines.flow.Flow

interface AuthorizationDataSource {

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