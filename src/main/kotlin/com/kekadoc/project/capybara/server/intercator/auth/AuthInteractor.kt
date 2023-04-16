package com.kekadoc.project.capybara.server.intercator.auth

import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationRequest
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationResponse

interface AuthInteractor {

    suspend fun authorize(request: AuthorizationRequest): Result<AuthorizationResponse>

}