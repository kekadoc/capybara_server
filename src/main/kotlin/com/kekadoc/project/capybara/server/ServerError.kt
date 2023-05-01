package com.kekadoc.project.capybara.server

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.Identifier
import io.ktor.http.*

object ServerError {

    fun tokenExpired(): Nothing = throw HttpException(
        statusCode = HttpStatusCode.Unauthorized,
        message = "Token expired",
    )

    fun tokenInvalid(): Nothing = throw HttpException(
        statusCode = HttpStatusCode.Unauthorized,
        message = "Token invalid",
    )

    fun userNotFound(userId: Identifier): Nothing = throw HttpException(
        statusCode = HttpStatusCode.NotFound,
        message = "User by $userId not found",
    )

    fun userNotFound(login: String): Nothing = throw HttpException(
        statusCode = HttpStatusCode.NotFound,
        message = "User by login $login not found",
    )

    fun userDuplicate(login: String): Throwable = HttpException(
        statusCode = HttpStatusCode.Conflict,
        message = "User with login already exist",
    )

}