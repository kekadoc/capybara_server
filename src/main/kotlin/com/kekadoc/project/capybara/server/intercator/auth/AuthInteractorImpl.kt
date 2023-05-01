package com.kekadoc.project.capybara.server.intercator.auth

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.common.secure.Hash
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.auth.RefreshTokenValidation
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.secure.UserSalt
import com.kekadoc.project.capybara.server.intercator.requireUser
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationRequest
import com.kekadoc.project.capybara.server.routing.api.auth.model.AuthorizationResponse
import com.kekadoc.project.capybara.server.routing.api.auth.model.RefreshTokensRequest
import io.ktor.http.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.single
import java.util.*

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
        println("___authorize $request")
        val hashedPassword = Hash.hash(
            value = request.password,
            salt = UserSalt.get(
                id = user.id,
                login = user.login,
            )
        )
        val testHash = Hash.hash(
            value = "123",
            salt = UserSalt.get(
                id = UUID.fromString("3bcb093e-6a0e-4431-94b0-a25d8bc5ba29"),
                login = "OlegAdmin",
            )
        )
        println("___authorize hashedPassword=$hashedPassword")
        println("___authorize user.password=${user.password}")
        println("___authorize testHash=${testHash}")
        if (hashedPassword != user.password) {
            throw HttpException(statusCode = HttpStatusCode.Unauthorized, "bad cred")
        }
        val tokens = authorizationRepository.authorizeUser(user).single()


        AuthorizationResponse(
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
        )

    }

    override suspend fun refreshToken(
        request: RefreshTokensRequest,
    ): Result<AuthorizationResponse> = runCatching {
        when (val validation = authorizationRepository.validateRefreshToken(request.refreshToken).single()) {
            is RefreshTokenValidation.Expired -> {
                throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Expired")
            }
            is RefreshTokenValidation.Invalid -> {
                throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Invalid")
            }
            is RefreshTokenValidation.Valid -> {
                val userId = validation.userId
                val login = validation.login
                val user = combine(
                    usersRepository.getUserByLogin(
                        login = login,
                    ),
                    usersRepository.getUserById(
                        id = UUID.fromString(userId.toString()),
                    )
                ) { userByLogin, userById ->
                    when {
                        userByLogin == null || userById == null -> {
                            throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Invalid")
                        }
                        userByLogin.id != userById.id -> {
                            throw HttpException(statusCode = HttpStatusCode.Unauthorized, "Invalid")
                        }
                        else -> {
                            userById
                        }
                    }
                }
                    .requireUser()
                    .single()
                val authorization = authorizationRepository.authorizeUser(user).single()
                AuthorizationResponse(
                    accessToken = authorization.accessToken,
                    refreshToken = authorization.refreshToken,
                )
            }
        }
    }

}