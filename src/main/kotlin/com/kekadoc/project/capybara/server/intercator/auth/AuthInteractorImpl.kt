package com.kekadoc.project.capybara.server.intercator.auth

import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.data.source.converter.dto.ProfileDtoConverter
import com.kekadoc.project.capybara.server.intercator.requireUser
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationRequest
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationResponse
import kotlinx.coroutines.flow.single

class AuthInteractorImpl(
    private val authorizationRepository: AuthorizationRepository,
    private val usersRepository: UsersRepository,
) : AuthInteractor {

    override suspend fun authorize(
        request: AuthorizationRequest,
    ): Result<AuthorizationResponse> = kotlin.runCatching {

        val user = usersRepository.getUserByLogin(
            login = request.login,
        )
            .requireUser()
            .single()
        val authorization = authorizationRepository.authorizeUser(
            login = user.login,
            password = request.password,
        )
            .single()

        AuthorizationResponse(
            token = authorization.token,
            profile = authorization.user.profile.let(ProfileDtoConverter::revert),
        )

    }

}