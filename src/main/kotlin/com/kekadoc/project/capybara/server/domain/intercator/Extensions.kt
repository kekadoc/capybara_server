package com.kekadoc.project.capybara.server.domain.intercator

import com.kekadoc.project.capybara.server.common.exception.HttpException
import com.kekadoc.project.capybara.server.domain.model.Message
import com.kekadoc.project.capybara.server.domain.model.Profile
import com.kekadoc.project.capybara.server.domain.model.User
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

fun Flow<User?>.requireUser(): Flow<User> = map { user ->
    user ?: throw HttpException(HttpStatusCode.NotFound)
}

fun Flow<User?>.requireAuthorizedUser(): Flow<User> = map { user ->
    user ?: throw HttpException(HttpStatusCode.Unauthorized)
}

fun Flow<User>.requireAdminUser(): Flow<User> = onEach { user ->
    if (user.profile.type != Profile.Type.ADMIN)
        throw HttpException(HttpStatusCode.Forbidden)
}

fun Flow<User>.requireSpeakerUser(): Flow<User> = onEach { user ->
    if (user.profile.type != Profile.Type.SPEAKER)
        throw HttpException(HttpStatusCode.Forbidden)
}

fun Flow<Message?>.orNotFoundError(): Flow<Message> = map { message ->
    message ?: throw HttpException(HttpStatusCode.NotFound)
}

fun Flow<Message>.requireAddressee(user: User): Flow<Message> = onEach { notification ->
    if (!notification.addresseeUserIds.contains(user.id) && !notification.addresseeGroupIds.any { user.groupIds.contains(it) }) {
        throw HttpException(
            statusCode = HttpStatusCode.Forbidden,
            message = "The message is only available to the recipient ${notification.addresseeUserIds}",
        )
    }
}