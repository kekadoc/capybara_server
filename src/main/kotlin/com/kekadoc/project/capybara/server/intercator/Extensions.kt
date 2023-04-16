package com.kekadoc.project.capybara.server.intercator

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.data.model.Message
import com.kekadoc.project.capybara.server.data.model.user.ProfileType
import com.kekadoc.project.capybara.server.data.model.user.User
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

fun Flow<Message>.requireAuthor(user: User): Flow<Message> {
    return onEach { message ->
        if (message.authorId != user.id) {
            throw HttpException(HttpStatusCode.Forbidden)
        }
    }
}

fun Flow<User?>.requireAuthorizedUser(): Flow<User> = map { user ->
    user ?: throw HttpException(HttpStatusCode.Unauthorized)
}

fun Flow<User>.requireAdminUser(): Flow<User> = onEach { user ->
    if (user.profile.type != ProfileType.ADMIN)
        throw HttpException(HttpStatusCode.Forbidden)
}

fun Flow<Message?>.orNotFoundError(): Flow<Message> = map { message ->
    message ?: throw HttpException(HttpStatusCode.NotFound)
}