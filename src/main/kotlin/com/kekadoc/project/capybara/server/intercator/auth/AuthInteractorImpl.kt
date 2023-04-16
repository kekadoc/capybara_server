package com.kekadoc.project.capybara.server.intercator.auth

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.routing.api.auth.errors.UserNotRegisteredException
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationRequest
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationResponse
import io.ktor.http.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.single

class AuthInteractorImpl(
    private val userRepository: UsersRepository,
) : AuthInteractor {

    override suspend fun authorize(
        request: AuthorizationRequest,
    ): Result<AuthorizationResponse> = kotlin.runCatching {
        val user = userRepository.authorizeUser(
            login = request.login,
            password = request.password,
            pushToken = request.pushToken,
        )
            .catch { error ->
                when (error) {
                    is UserNotRegisteredException -> throw HttpException(HttpStatusCode.NotFound)
                    else -> throw error
                }
            }
            .single()

        AuthorizationResponse(
            token = user.authToken,
            profile = user.profile
        )
    }

}