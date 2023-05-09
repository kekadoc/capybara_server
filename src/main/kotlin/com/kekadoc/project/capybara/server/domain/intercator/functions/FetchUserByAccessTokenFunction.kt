package com.kekadoc.project.capybara.server.domain.intercator.functions

import com.kekadoc.project.capybara.server.common.arch.annotation.BusinessLogic
import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.repository.auth.AccessTokenValidation
import com.kekadoc.project.capybara.server.data.repository.auth.AuthorizationRepository
import com.kekadoc.project.capybara.server.data.repository.user.UsersRepository
import com.kekadoc.project.capybara.server.domain.model.Token
import com.kekadoc.project.capybara.server.domain.model.User
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

@BusinessLogic
class FetchUserByAccessTokenFunction constructor(
    private val authorizationRepository: AuthorizationRepository,
    private val userRepository: UsersRepository,
) {

    @Throws(HttpException::class)
    fun fetchUser(accessToken: Token): Flow<User> {
        return authorizationRepository.fetchUser(accessToken)
            .flatMapLatest { validation ->
                when (validation) {
                    is AccessTokenValidation.Expired -> {
                        throw HttpException(
                            statusCode = HttpStatusCode.Unauthorized,
                            message = "Access expired"
                        )
                    }
                    is AccessTokenValidation.Invalid -> {
                        throw HttpException(
                            statusCode = HttpStatusCode.Unauthorized,
                            message = validation.error?.localizedMessage ?: "Invalid access token"
                        )
                    }
                    is AccessTokenValidation.Valid -> {
                        userRepository.getUserById(validation.userId)
                            .map { user ->
                                user ?: throw HttpException(
                                    statusCode = HttpStatusCode.Unauthorized,
                                    message = "User not found by this access token",
                                )
                            }
                    }
                }
            }
    }

}